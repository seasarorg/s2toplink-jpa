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

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;
import org.seasar.toplink.jpa.entity.Customer;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class PersistenceUnitInfoFactoryImplTest extends S2TestCase {

    private PersistenceUnitInfoFactory unitInfoFactory;
    
    private PersistenceUnitInfo unitInfo;
    
    private static final String MAPPING_FILE_NAME = "org/seasar/toplink/jpa/entity/CustomerOrm.xml";
    
    private static final String CLASS_NAME = Customer.class.getName();
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(PersistenceUnitInfoFactoryImplTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.PersistenceUnitInfoFactoryImpl#init()} のためのテスト・メソッド。
     */
    public void testInit() {
        PersistenceUnitInfoFactoryImpl impl = PersistenceUnitInfoFactoryImpl.class.cast(unitInfoFactory);
        impl.init();
        Map<String, SEPersistenceUnitInfo> map = impl.sePersistenceUnitInfoMap;
        assertEquals(1, map.size());
        SEPersistenceUnitInfo info = map.get("persistenceUnit");
        assertEquals("persistenceUnit", info.getPersistenceUnitName());
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.PersistenceUnitInfoFactoryImpl#getPersistenceUnitInfo(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetPersistenceUnitInfo() {
        PersistenceUnitInfo info = unitInfoFactory.getPersistenceUnitInfo("persistenceUnit");
        assertEquals("persistenceUnit", info.getPersistenceUnitName());
        assertEquals(EntityManagerFactoryProvider.class.getName(), info.getPersistenceProviderClassName());
        assertEquals(PersistenceUnitTransactionType.JTA, info.getTransactionType());
        assertTrue(info.getJtaDataSource() instanceof DataSourceImpl);
        assertNull(info.getNonJtaDataSource());
        List<String> filelist = info.getMappingFileNames();
        assertTrue(filelist.contains(MAPPING_FILE_NAME));
        assertNotNull(info.getPersistenceUnitRootUrl());
        List<String> classList = info.getManagedClassNames();
        assertEquals(1, classList.size());
        assertEquals(CLASS_NAME, classList.get(0));
        assertTrue(info.excludeUnlistedClasses());
        Properties prop = info.getProperties();
        assertEquals("org.seasar.toplink.jpa.platform.server.S2ServerPlatform", prop.getProperty("toplink.target-server"));
        assertEquals("org.seasar.toplink.jpa.platform.database.HSQLDBPlatform", prop.getProperty("toplink.target-database"));
        assertEquals("FINE", prop.getProperty("toplink.logging.level"));
        assertEquals("org.seasar.toplink.jpa.platform.server.S2ServerPlatform", prop.getProperty("toplink.target-server"));
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.PersistenceUnitInfoFactoryImpl#addAutoDetectResult(javax.persistence.spi.PersistenceUnitInfo)} のためのテスト・メソッド。
     */
    public void testAddAutoDetectResult() {
        assertFalse(unitInfo.getMappingFileNames().contains(MAPPING_FILE_NAME));
        assertFalse(unitInfo.getManagedClassNames().contains(CLASS_NAME));
        unitInfoFactory.addAutoDetectResult(unitInfo);
        assertTrue(unitInfo.getMappingFileNames().contains(MAPPING_FILE_NAME));
        assertTrue(unitInfo.getManagedClassNames().contains(CLASS_NAME));
    }

}
