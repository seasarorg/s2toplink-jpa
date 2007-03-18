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
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.ClassDetails;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaved;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class JpaInstrumentationImpl implements JpaInstrumentation {
        
    private static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    private static final ProtectionDomain protectionDomain;

    private static Method defineClassMethod;
    
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

    }

    /**
     * @see org.seasar.toplink.jpa.JpaInstrumentation#addTransformer(javax.persistence.spi.ClassTransformer,
     *      java.lang.ClassLoader)
     */
    public void addTransformer(ClassTransformer classTransformer,
            ClassLoader classLoader) {
        if(!TopLinkWeaver.class.isInstance(classTransformer)) {
            return;
        }
        TopLinkWeaver weaver = TopLinkWeaver.class.cast(classTransformer);
        @SuppressWarnings("unchecked")
        Map<String, ClassDetails> map = (Map<String, ClassDetails>) weaver.getClassDetailsMap();
        for (String classPath : map.keySet()) {
            String className = classPath.replace('/', '.');
            if (isLoaded(classLoader, className)) {
                continue;
            }
            
            ResourceData data = getResourceData(classLoader, classPath);
            if (data == null) {
                continue;
            }
            
            byte[] temp = InputStreamUtil.getBytes(data.getIn());
            try {
                byte[] bytes = classTransformer.transform(data.getLoader(),
                        classPath, null, protectionDomain, temp);
                if (bytes != null) {
                    defineClassMethod.invoke(
                            data.getLoader(),
                            className,
                            bytes,
                            0,
                            bytes.length,
                            protectionDomain);
                }
            } catch (IllegalAccessException e) {
                if (data != null && data.getLoader() != null) {
                    throw new IllegalAccessRuntimeException(data.getLoader().getClass(), e);
                } else {
                    throw new RuntimeException(e);
                }
            } catch (InvocationTargetException e) {
                if (data != null && data.getLoader() != null) {
                    throw new InvocationTargetRuntimeException(data.getLoader().getClass(), e);
                } else {
                    throw new RuntimeException(e);
                }
            } catch (IllegalClassFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 引数のClassLoaderで指定したクラスがロードできるかチェックします。
     * @param loader チェック対象ClassLoader
     * @param className チェック対象クラス名
     * @return ロード可能の場合true
     */
    protected boolean isLoaded(final ClassLoader loader, final String className) {
        try {
            return ClassLoaderUtil.findLoadedClass(loader, className) != null;
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }
        
    /**
     * クラス名のStringとClassLoaderを渡して、ClassをロードできるClassLoaderとClassオブジェクトのInputStreamを取得します。
     * 
     * @param loader ClassLoaderオブジェクト
     * @param className クラス名
     * @return 指定したクラスをロードできるClassLoaderと、指定したクラスのInputStreamを持つResourceData
     */
    protected ResourceData getResourceData(ClassLoader loader, String className) {
        className = className + ".class";
        String weavedClassName = TopLinkWeaved.class.getName().replace('.', '/') + ".class";
        for (ClassLoader cl : getClassLoaderList(loader)) {
            if (cl.getResource(weavedClassName) == null) {
                continue;
            }
            InputStream in = cl.getResourceAsStream(className);
            if (in != null) {
                ResourceData data = new ResourceData();
                data.setLoader(cl);
                data.setIn(in);
                return data;
            }
        }
        return null;
    }
    
    /**
     * ClassLoaderを受け取り、引数のClassLoaderから親ClassLoaderを検索し、取得できた全てのClassLoaderをListで返します。
     * 
     * @param loader ClassLoaderオブジェクト
     * @return 引数のClassLoaderを含み、取得出来る全ての親ClassLoaderを含むList。
     *         親が子の前に来るように順番付けしている。
     */
    protected List<ClassLoader> getClassLoaderList(ClassLoader loader) {
        LinkedList<ClassLoader> list = CollectionsUtil.newLinkedList();
        list.add(loader);
        while (loader.getParent() != null && loader.getParent() != loader) {
            list.addFirst(loader.getParent());
            loader = loader.getParent();
        }
        return list;
    }
    
    /**
     * InputStremとClassLoaderをセットで保持するクラス
     * @author Hidenoshin Yoshida
     *
     */
    protected class ResourceData {
        
        private InputStream in;
        
        private ClassLoader loader;

        /**
         * InputStreamオブジェクトを返します。
         * @return in InputStreamオブジェクト
         */
        public InputStream getIn() {
            return in;
        }

        /**
         * InputStreamオブジェクトを設定します。
         * @param in 設定するInputStreamオブジェクト
         */
        public void setIn(InputStream in) {
            this.in = in;
        }

        /**
         * ClassLoaderオブジェクトを返します。
         * @return loader ClassLoaderオブジェクト
         */
        public ClassLoader getLoader() {
            return loader;
        }

        /**
         * ClassLoaderオブジェクトを設定します。
         * @param loader 設定するClassLoaderオブジェクト
         */
        public void setLoader(ClassLoader loader) {
            this.loader = loader;
        }
    }

}
