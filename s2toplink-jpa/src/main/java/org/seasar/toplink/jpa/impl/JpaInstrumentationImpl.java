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

import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Map;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.ClassDetails;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaved;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class JpaInstrumentationImpl implements JpaInstrumentation {
        
    private static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    private static final String FIND_RESOURCE_METHOD_NAME = "findResource";

    private static final ProtectionDomain protectionDomain;

    private static Method defineClassMethod;
    
    private static Method findResourceMethod;

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
                final Class<?>[] paramTypes = new Class<?>[] { String.class,
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
                final Class<?>[] paramTypes = new Class[] {String.class};
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

    /**
     * @see org.seasar.toplink.jpa.JpaInstrumentation#addTransformer(javax.persistence.spi.ClassTransformer,
     *      java.lang.ClassLoader)
     */
    public void addTransformer(final ClassTransformer transformer, final ClassLoader ignore) {
        if (!TopLinkWeaver.class.isInstance(transformer)) {
            return;
        }
        final TopLinkWeaver weaver = TopLinkWeaver.class.cast(transformer);
        @SuppressWarnings("unchecked")
        final Map<String, ClassDetails> map = (Map<String, ClassDetails>) weaver
                .getClassDetailsMap();
        final ClassLoader loader =  TopLinkWeaved.class.getClassLoader();
        for (final String classPath : map.keySet()) {
            final String  className = classPath.replace("/", ".");
            if (isLoaded(loader, className)) {
                continue;
            }
            final InputStream is = ResourceUtil.getResourceAsStream(classPath + ".class");
            byte[] bytes = null;
            try {
                bytes = InputStreamUtil.getBytes(is);
            } finally {
                InputStreamUtil.close(is);
            }
            final byte[] transBytes = transform(transformer, loader, classPath,
                    null, null, bytes);
            if (transBytes != null) {
                ReflectionUtil.invoke(defineClassMethod, loader, className,
                        transBytes, 0, transBytes.length, protectionDomain);
            }
        }
    }

    // TODO
    protected byte[] transform(final ClassTransformer transformer,
            final ClassLoader loader, final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer) {
        try {
            return transformer.transform(loader, className,
                    classBeingRedefined, protectionDomain, classfileBuffer);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO
    protected boolean isLoaded(final ClassLoader loader, final String className) {
        try {
            return ClassLoaderUtil.findLoadedClass(loader, className) != null;
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

}
