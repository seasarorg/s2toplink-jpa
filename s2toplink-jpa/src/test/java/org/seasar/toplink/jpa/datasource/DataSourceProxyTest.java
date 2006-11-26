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
package org.seasar.toplink.jpa.datasource;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class DataSourceProxyTest extends S2TestCase {

    private DataSource dsProxy;
    
    private DataSource mockDs;
    
    private MockInterceptor mock;
    
    

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(DataSourceProxyTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#DataSourceProxy(java.lang.String)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testDataSourceProxy() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = DataSourceProxy.class.getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("mockDs", field.get(dsProxy));
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#getConnection()} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testGetConnection() throws SQLException {
        dsProxy.getConnection();
        assertTrue(mock.isInvoked("getConnection"));
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#getConnection(java.lang.String, java.lang.String)} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testGetConnectionStringString() throws SQLException {
        dsProxy.getConnection("id", "password");
        assertTrue(mock.isInvoked("getConnection"));
        assertEquals("id", mock.getArgs("getConnection")[0]);
        assertEquals("password", mock.getArgs("getConnection")[1]);
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#getLogWriter()} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testGetLogWriter() throws SQLException {
        dsProxy.getLogWriter();
        assertTrue(mock.isInvoked("getLogWriter"));
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#getLoginTimeout()} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testGetLoginTimeout() throws SQLException {
        dsProxy.getLoginTimeout();
        assertTrue(mock.isInvoked("getLoginTimeout"));
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#setLogWriter(java.io.PrintWriter)} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testSetLogWriter() throws SQLException {
        PrintWriter out = new PrintWriter(new StringWriter());
        dsProxy.setLogWriter(out);
        assertTrue(mock.isInvoked("setLogWriter"));
        assertEquals(out, mock.getArgs("setLogWriter")[0]);
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#setLoginTimeout(int)} のためのテスト・メソッド。
     * @throws SQLException 
     */
    public void testSetLoginTimeout() throws SQLException {
        dsProxy.setLoginTimeout(20);
        assertTrue(mock.isInvoked("setLoginTimeout"));
        assertEquals(20, mock.getArgs("setLoginTimeout")[0]);
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxy#getDataSource()} のためのテスト・メソッド。
     */
    public void testGetDataSource() {
        DataSourceProxy proxy = DataSourceProxy.class.cast(dsProxy);
        assertEquals(mockDs, proxy.getDataSource());
    }

}
