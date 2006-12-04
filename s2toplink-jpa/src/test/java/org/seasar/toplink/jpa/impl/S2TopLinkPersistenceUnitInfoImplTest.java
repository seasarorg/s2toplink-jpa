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

import javax.persistence.spi.ClassTransformer;
import javax.sql.DataSource;

import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

import org.seasar.extension.datasource.impl.SingletonDataSourceProxy;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkPersistenceUnitInfoImplTest extends S2TestCase {
    
    private S2TopLinkPersistenceUnitInfo unitInfo;
    
    private ClassTransformer classTransformer;
    
    private MockInterceptor instMock;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(S2TopLinkPersistenceUnitInfoImplTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkPersistenceUnitInfoImpl#addTransformer(javax.persistence.spi.ClassTransformer)} のためのテスト・メソッド。
     */
    public void testAddTransformerClassTransformer() {
        unitInfo.addTransformer(classTransformer);
        assertTrue(instMock.isInvoked("addTransformer"));
        Object[] args = instMock.getArgs("addTransformer");
        assertEquals(classTransformer, args[0]);
        assertEquals(Thread.currentThread().getContextClassLoader(), args[1]);
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkPersistenceUnitInfoImpl#getNewTempClassLoader()} のためのテスト・メソッド。
     */
    public void testGetNewTempClassLoader() {
        ClassLoader loader = unitInfo.getNewTempClassLoader();
        assertTrue(loader instanceof S2TopLinkTempClassLoader);
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkPersistenceUnitInfoImpl#setJtaDataSource(javax.sql.DataSource)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testSetJtaDataSourceDataSource() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        DataSourceImpl dsImpl = new DataSourceImpl("dataSourceProxy", null, null, null);
        unitInfo.setJtaDataSource(dsImpl);
        DataSource jtaDs = unitInfo.getJtaDataSource();
        assertTrue(jtaDs instanceof SingletonDataSourceProxy);
        Field field = SingletonDataSourceProxy.class.getDeclaredField("actualDataSourceName");
        field.setAccessible(true);
        assertEquals("dataSourceProxy", field.get(jtaDs));
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkPersistenceUnitInfoImpl#setNonJtaDataSource(javax.sql.DataSource)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testSetNonJtaDataSourceDataSource() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        DataSourceImpl dsImpl = new DataSourceImpl("dataSourceProxy", null, null, null);
        unitInfo.setNonJtaDataSource(dsImpl);
        DataSource nonJtaDs = unitInfo.getNonJtaDataSource();
        assertTrue(nonJtaDs instanceof SingletonDataSourceProxy);
        Field field = SingletonDataSourceProxy.class.getDeclaredField("actualDataSourceName");
        field.setAccessible(true);
        assertEquals("dataSourceProxy", field.get(nonJtaDs));
    }

}
