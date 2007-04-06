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
import java.security.SecureClassLoader;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;

/**
 * @author taedium
 * 
 */
class TempClassLoader extends SecureClassLoader {

    public TempClassLoader(final ClassLoader parent) {
        super(parent);
    }

    @Override
    protected synchronized Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        if (name.startsWith("java.") || name.startsWith("javax.")) {
            return super.loadClass(name, resolve);
        }
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            clazz = findClass(name);
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final String path = ClassUtil.getResourcePath(name);
        final InputStream in = getResourceAsStream(path);
        if (in == null) {
            throw new ClassNotFoundException(name);
        }
        final byte[] classBytes = InputStreamUtil.getBytes(in);
        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
