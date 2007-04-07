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
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import oracle.toplink.essentials.internal.weaving.TopLinkWeaved;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class InstrumentationImpl implements Instrumentation {

    private static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    private static final Method defineClassMethod;

    static {
        defineClassMethod = ReflectionUtil.getDeclaredMethod(ClassLoader.class,
                DEFINE_CLASS_METHOD_NAME, String.class, byte[].class,
                int.class, int.class);
        defineClassMethod.setAccessible(true);
    }

    protected final List<String> classNames;

    public InstrumentationImpl(final List<String> classNames) {
        this.classNames = classNames;
    }

    public void addTransformer(final ClassFileTransformer transformer) {
        for (final String className : classNames) {
            final String resourcePath = ClassUtil.getResourcePath(className);
            final ClassLoader classLoader = detectClassLoader(resourcePath);
            if (isLoaded(classLoader, className)) {
                continue;
            }
            final InputStream is = ResourceUtil
                    .getResourceAsStream(resourcePath);
            final byte[] temp = InputStreamUtil.getBytes(is);
            byte[] bytes = null;
            try {
                bytes = transformer.transform(classLoader, className.replace(
                        ".", "/"), null, null, temp);
            } catch (final IllegalClassFormatException e) {
                throw new RuntimeException(e);
            }
            if (bytes != null) {
                ReflectionUtil.invoke(defineClassMethod, classLoader,
                        className, bytes, 0, bytes.length);
            }
        }
    }

    protected ClassLoader detectClassLoader(final String resourcePath) {
        final String topLinkPath = ClassUtil
                .getResourcePath(TopLinkWeaved.class);
        for (final ClassLoader cl : getClassLoaderList(Thread.currentThread()
                .getContextClassLoader())) {
            if (cl.getResource(resourcePath) != null) {
                if (cl.getResource(topLinkPath) != null) {
                    return cl;
                }
            }
        }
        return null;
    }

    /**
     * ClassLoaderを受け取り、引数のClassLoaderから親ClassLoaderを検索し、取得できた全てのClassLoaderをListで返します。
     * 
     * @param loader
     *            ClassLoaderオブジェクト
     * @return 引数のClassLoaderを含み、取得出来る全ての親ClassLoaderを含むList。
     *         親が子の前に来るように順番付けしている。
     */
    protected List<ClassLoader> getClassLoaderList(ClassLoader loader) {
        final LinkedList<ClassLoader> list = CollectionsUtil.newLinkedList();
        list.add(loader);
        while (loader.getParent() != null && loader.getParent() != loader) {
            list.addFirst(loader.getParent());
            loader = loader.getParent();
        }
        return list;
    }

    protected boolean isLoaded(final ClassLoader loader, final String className) {
        try {
            return ClassLoaderUtil.findLoadedClass(loader, className) != null;
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Class[] getAllLoadedClasses() {
        throw new UnsupportedOperationException("getAllLoadedClasses");
    }

    @SuppressWarnings("unchecked")
    public Class[] getInitiatedClasses(final ClassLoader loader) {
        throw new UnsupportedOperationException("getInitiatedClasses");
    }

    public long getObjectSize(final Object objectToSize) {
        throw new UnsupportedOperationException("getObjectSize");
    }

    public boolean isRedefineClassesSupported() {
        throw new UnsupportedOperationException("isRedefineClassesSupported");
    }

    public void redefineClasses(final ClassDefinition[] definitions)
            throws ClassNotFoundException, UnmodifiableClassException {
        throw new UnsupportedOperationException("redefineClasses");
    }

    public boolean removeTransformer(final ClassFileTransformer transformer) {
        throw new UnsupportedOperationException("removeTransformer");
    }

}
