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

import javax.naming.Name;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.datasource.DataSourceProxy;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class DataSourceFactoryTest extends S2TestCase {

    private Name name;
    
    private DataSourceProxyFactory dataSourceFactory;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(DataSourceFactoryTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxyFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)} のためのテスト・メソッド。
     * @throws Exception 
     */
    public void testGetObjectInstance() throws Exception {
        Object obj = dataSourceFactory.getObjectInstance(
                null, name, null, null);
        assertTrue(obj instanceof DataSourceProxy);
    }

}
