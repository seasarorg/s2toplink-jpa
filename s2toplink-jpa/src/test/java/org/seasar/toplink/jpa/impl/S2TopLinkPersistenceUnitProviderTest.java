/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;

import org.junit.runner.RunWith;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 *
 */
@RunWith(Seasar2.class)
public class S2TopLinkPersistenceUnitProviderTest {
    
    private PersistenceUnitProvider localUnitProvider;

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkPersistenceUnitProvider#createEntityManagerFactory(java.lang.String)} のためのテスト・メソッド。
     */
    public void testCreateEntityManagerFactory() {
//        assertTrue(provider instanceof S2TopLinkPersistenceUnitProvider);
        EntityManagerFactory factory = localUnitProvider.createEntityManagerFactory("persistenceUnit");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = localUnitProvider.createEntityManagerFactory("persistenceUnit2");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = localUnitProvider.createEntityManagerFactory("persistenceUnit3");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = localUnitProvider.createEntityManagerFactory("persistenceUnit4");
        assertNull(factory);
    }

}
