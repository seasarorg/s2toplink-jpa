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

import java.lang.reflect.Field;

import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;
import org.seasar.toplink.jpa.entity.Customer;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2JavaSECMPInitializerTest extends S2TestCase {
    
    private static final String MAPPING_FILE_NAME = "org/seasar/toplink/jpa/entity/CustomerOrm.xml";
    
    private JavaSECMPInitializer javaSECMPInitializer;

    private PersistenceUnitInfoFactory persistenceUnitInfoFactory;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include("s2toplink-jpa.dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2JavaSECMPInitializer#getJavaSECMPInitializer(java.lang.String, java.util.Map)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testGetJavaSECMPInitializerStringMap() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = JavaSECMPInitializer.class.getDeclaredField("javaSECMPInitializer");
        field.setAccessible(true);
        field.set(null, null);
        javaSECMPInitializer = S2JavaSECMPInitializer.getJavaSECMPInitializer("s2toplink-jpa.dicon", null);
        assertEquals(field.get(null), javaSECMPInitializer);
        javaSECMPInitializer = new S2JavaSECMPInitializer();
        field.set(null, javaSECMPInitializer);
        assertEquals(javaSECMPInitializer, S2JavaSECMPInitializer.getJavaSECMPInitializer("s2toplink-jpa.dicon", null));
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2JavaSECMPInitializer#initializeFromContainer(java.lang.String, java.util.Map)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testInitializeFromContainer() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = JavaSECMPInitializer.class.getDeclaredField("javaSECMPInitializer");
        field.setAccessible(true);
        field.set(null, null);
        S2JavaSECMPInitializer.initializeFromContainer("s2toplink-jpa.dicon", null);
        assertNotNull(field.get(null));
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2JavaSECMPInitializer#callPredeploy(oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo, java.util.Map)} のためのテスト・メソッド。
     */
    public void testCallPredeploySEPersistenceUnitInfoMap() {
        PersistenceUnitInfo unitInfo = persistenceUnitInfoFactory.getPersistenceUnitInfo("persistenceUnit");
        unitInfo.getManagedClassNames().clear();
        unitInfo.getMappingFileNames().clear();
        S2JavaSECMPInitializer.class.cast(javaSECMPInitializer).callPredeploy(SEPersistenceUnitInfo.class.cast(unitInfo), null);
        assertTrue(unitInfo.getManagedClassNames().contains(Customer.class.getName()));
        assertTrue(unitInfo.getMappingFileNames().contains(MAPPING_FILE_NAME));
    }

}
