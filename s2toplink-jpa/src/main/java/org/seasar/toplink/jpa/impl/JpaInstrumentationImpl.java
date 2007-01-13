/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.toplink.jpa.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.ClassDetails;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class JpaInstrumentationImpl implements JpaInstrumentation {
    
    public static final String DEFINE_CLASS_METHOD_NAME = "defineClass";
    
    public static final String FIND_RESOURCE_METHOD_NAME = "findResource";

    // static fields
    protected static final ProtectionDomain protectionDomain;

    protected static Method defineClassMethod;
    
    protected static Method findResourceMethod;

    // static initializer
    static {
        protectionDomain = AccessController
                .doPrivileged(new PrivilegedAction<ProtectionDomain>() {
                    public ProtectionDomain run() {
                        return JpaInstrumentationImpl.class
                                .getProtectionDomain();
                    }
                });

        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                final Class[] paramTypes = new Class[] { String.class,
                        byte[].class, int.class, int.class,
                        ProtectionDomain.class };
                try {
                    final Class<?> loader = ClassUtil.forName(ClassLoader.class
                            .getName());
                    defineClassMethod = loader.getDeclaredMethod(
                            DEFINE_CLASS_METHOD_NAME, paramTypes);
                    defineClassMethod.setAccessible(true);
                } catch (final NoSuchMethodException e) {
                    throw new NoSuchMethodRuntimeException(ClassLoader.class,
                            DEFINE_CLASS_METHOD_NAME, paramTypes, e);
                }
                return null;
            }
        });

        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                final Class[] paramTypes = new Class[] {String.class};
                try {
                    final Class<?> loader = ClassUtil.forName(ClassLoader.class
                            .getName());
                    findResourceMethod = loader.getDeclaredMethod(
                            FIND_RESOURCE_METHOD_NAME, paramTypes);
                    findResourceMethod.setAccessible(true);
                } catch (final NoSuchMethodException e) {
                    throw new NoSuchMethodRuntimeException(ClassLoader.class,
                            FIND_RESOURCE_METHOD_NAME, paramTypes, e);
                }
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void addTransformer(ClassTransformer classTransformer,
            ClassLoader classLoader) {
        if (classTransformer instanceof TopLinkWeaver) {
            TopLinkWeaver weaver = TopLinkWeaver.class.cast(classTransformer);
            Map<String, ClassDetails> map = (Map<String, ClassDetails>) weaver
                    .getClassDetailsMap();
            for (String className : map.keySet()) {
                try {
                    if (ClassLoaderUtil.findLoadedClass(classLoader, className.replace('/', '.')) != null) {
                        continue;
                    }
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundRuntimeException(e);
                }
                ResourceData data = getResourceData(classLoader, className);
                if (data == null) {
                    continue;
                }
                try {
                    byte[] temp = getClassBytes(data);
                    byte[] bytes = classTransformer.transform(data.getLoader(),
                                className, null, protectionDomain, temp);
                    if (bytes != null) {
                        defineClassMethod.invoke(
                            data.getLoader(),
                            className.replace('/', '.'),
                            bytes,
                            0,
                            bytes.length,
                            protectionDomain);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalAccessRuntimeException(data.getLoader().getClass(), e);
                } catch (InvocationTargetException e) {
                    throw new InvocationTargetRuntimeException(data.getLoader().getClass(), e);
                } catch (IllegalClassFormatException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }

            }
        }
    }

    private byte[] getClassBytes(ResourceData data)
            throws IOException {
        ByteArrayOutputStream out = null;
        InputStream in = null;
        try {
            out = new ByteArrayOutputStream();
            in = data.getIn();
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }

            return out.toByteArray();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    private ResourceData getResourceData(ClassLoader loader, String className) {
        Object ret = null;
        className = className + ".class";
        for (ClassLoader cl : getClassLoaderList(loader)) {
            try {
                ret = findResourceMethod.invoke(cl, className);
                if (ret != null) {
                    URL url = URL.class.cast(ret);
                    ResourceData data = new ResourceData();
                    data.setLoader(cl);
                    try {
                        data.setIn(new BufferedInputStream(url.openStream()));
                    } catch (IOException e) {
                        throw new IORuntimeException(e);
                    }
                    return data;
                }
            } catch (IllegalAccessException e) {
                throw new IllegalAccessRuntimeException(cl.getClass(), e);
            } catch (InvocationTargetException e) {
                throw new InvocationTargetRuntimeException(cl.getClass(), e);
            }
        }
        return null;
    }
    
    private List<ClassLoader> getClassLoaderList(ClassLoader loader) {
        LinkedList<ClassLoader> list = CollectionsUtil.newLinkedList();
        list.add(loader);
        while (loader.getParent() != null && loader.getParent() != loader) {
            list.addFirst(loader.getParent());
            loader = loader.getParent();
        }
        return list;
    }
    
    private class ResourceData {
        
        private InputStream in;
        
        private ClassLoader loader;

        /**
         * @return in
         */
        public InputStream getIn() {
            return in;
        }

        /**
         * @param in 設定する in
         */
        public void setIn(InputStream in) {
            this.in = in;
        }

        /**
         * @return loader
         */
        public ClassLoader getLoader() {
            return loader;
        }

        /**
         * @param loader 設定する loader
         */
        public void setLoader(ClassLoader loader) {
            this.loader = loader;
        }
        
        
    }

}
