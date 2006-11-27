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

import java.lang.reflect.Field;
import java.util.Enumeration;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class DataSourceProxyFactoryTest extends S2TestCase {
    
    private ObjectFactory factory;
    
    private class TestName implements Name {

        /* (non-Javadoc)
         * @see java.lang.Object#clone()
         */
        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "dataSource";
        }

        /**
         * 
         */
        private static final long serialVersionUID = 6003014495421287781L;

        /* (non-Javadoc)
         * @see javax.naming.Name#add(java.lang.String)
         */
        public Name add(String comp) throws InvalidNameException {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#add(int, java.lang.String)
         */
        public Name add(int posn, String comp) throws InvalidNameException {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#addAll(javax.naming.Name)
         */
        public Name addAll(Name suffix) throws InvalidNameException {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#addAll(int, javax.naming.Name)
         */
        public Name addAll(int posn, Name n) throws InvalidNameException {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#compareTo(java.lang.Object)
         */
        public int compareTo(Object obj) {
            return 0;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#endsWith(javax.naming.Name)
         */
        public boolean endsWith(Name n) {
            return false;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#get(int)
         */
        public String get(int posn) {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#getAll()
         */
        public Enumeration<String> getAll() {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#getPrefix(int)
         */
        public Name getPrefix(int posn) {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#getSuffix(int)
         */
        public Name getSuffix(int posn) {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#isEmpty()
         */
        public boolean isEmpty() {
            return false;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#remove(int)
         */
        public Object remove(int posn) throws InvalidNameException {
            return null;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#size()
         */
        public int size() {
            return 0;
        }

        /* (non-Javadoc)
         * @see javax.naming.Name#startsWith(javax.naming.Name)
         */
        public boolean startsWith(Name n) {
            return false;
        }
        
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(DataSourceProxyFactoryTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.datasource.DataSourceProxyFactory#getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable)} のためのテスト・メソッド。
     * @throws Exception 
     */
    public void testGetObjectInstance() throws Exception {
        Name name = new TestName();
        Object obj = factory.getObjectInstance(null, name, null, null);
        assertEquals(DataSourceProxy.class, obj.getClass());
        Field field = obj.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals(name.toString(), field.get(obj));
    }

}
