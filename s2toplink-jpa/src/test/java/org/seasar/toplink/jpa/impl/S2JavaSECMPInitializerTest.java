/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.lang.reflect.Field;

import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 * 
 */
@RunWith(Seasar2.class)
public class S2JavaSECMPInitializerTest {

    /**
     * {@link org.seasar.toplink.jpa.impl.S2JavaSECMPInitializer#getJavaSECMPInitializer(java.lang.String, java.util.Map)}
     * のためのテスト・メソッド。
     * 
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void testGetJavaSECMPInitializerStringMap()
            throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field field = JavaSECMPInitializer.class
                .getDeclaredField("javaSECMPInitializer");
        field.setAccessible(true);
        field.set(null, null);
        JavaSECMPInitializer javaSECMPInitializer = S2JavaSECMPInitializer
                .getJavaSECMPInitializer("s2toplink-jpa-preload.dicon", null);
        assertEquals(field.get(null), javaSECMPInitializer);
        javaSECMPInitializer = new S2JavaSECMPInitializer();
        field.set(null, javaSECMPInitializer);
        assertEquals(javaSECMPInitializer, S2JavaSECMPInitializer
                .getJavaSECMPInitializer("s2toplink-jpa-preload.dicon", null));
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2JavaSECMPInitializer#initializeFromContainer(java.lang.String, java.util.Map)}
     * のためのテスト・メソッド。
     * 
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void testInitializeFromContainer() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field field = JavaSECMPInitializer.class
                .getDeclaredField("javaSECMPInitializer");
        field.setAccessible(true);
        field.set(null, null);
        S2JavaSECMPInitializer.initializeFromContainer(
                "s2toplink-jpa-preload.dicon", null);
        assertNotNull(field.get(null));
    }

}
