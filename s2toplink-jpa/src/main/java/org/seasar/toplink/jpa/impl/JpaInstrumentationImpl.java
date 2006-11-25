/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Map;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.ClassDetails;
import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class JpaInstrumentationImpl implements JpaInstrumentation {

    public static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    // static fields
    protected static final ProtectionDomain protectionDomain;

    protected static Method defineClassMethod;

    // static initializer
    static {
        protectionDomain = (ProtectionDomain) AccessController
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
                    final Class loader = ClassUtil.forName(ClassLoader.class
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

    @SuppressWarnings("unchecked")
    public void addTransformer(ClassTransformer classTransformer,
            ClassLoader classLoader) {
        if (classTransformer instanceof TopLinkWeaver) {
            TopLinkWeaver weaver = TopLinkWeaver.class.cast(classTransformer);
            Map<String, ClassDetails> map = (Map<String, ClassDetails>) weaver
                    .getClassDetailsMap();
            for (String className : map.keySet()) {
                try {
                    byte[] temp = getClassBytes(className, classLoader);
                    byte[] bytes = classTransformer.transform(classLoader,
                            className, null, protectionDomain, temp);
                    if (bytes != null) {
                        defineClassMethod.invoke(
                                classLoader,
                                className.replace('/', '.'),
                                bytes,
                                0,
                                bytes.length,
                                protectionDomain);
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalClassFormatException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private byte[] getClassBytes(String className, ClassLoader loader)
            throws IOException {
        ByteArrayOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new ByteArrayOutputStream();
            in = new BufferedInputStream(loader.getResourceAsStream(className + ".class"));
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

}
