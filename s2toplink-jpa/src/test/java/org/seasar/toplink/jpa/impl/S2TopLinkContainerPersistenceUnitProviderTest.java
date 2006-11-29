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
import java.util.List;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.jpa.impl.PersistenceUnitManagerImpl;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkContainerPersistenceUnitProviderTest extends S2TestCase {
    
    private PersistenceUnitProvider provider;

    private PersistenceUnitManager unitManager;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(S2TopLinkContainerPersistenceUnitProviderTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkContainerPersistenceUnitProvider#register()} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @SuppressWarnings("unchecked")
    public void testRegister() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = PersistenceUnitManagerImpl.class.getDeclaredField("providers");
        field.setAccessible(true);
        List<PersistenceUnitProvider> providers = (List<PersistenceUnitProvider>) field.get(unitManager);
        providers.clear();
        S2TopLinkContainerPersistenceUnitProvider.class.cast(provider).register();
        assertEquals(1, providers.size());
        assertEquals(provider, providers.get(0));
        
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkContainerPersistenceUnitProvider#unregister()} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @SuppressWarnings("unchecked")
    public void testUnregister() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = PersistenceUnitManagerImpl.class.getDeclaredField("providers");
        field.setAccessible(true);
        List<PersistenceUnitProvider> providers = (List<PersistenceUnitProvider>) field.get(unitManager);
        assertEquals(1, providers.size());
        assertEquals(provider, providers.get(0));
        S2TopLinkContainerPersistenceUnitProvider.class.cast(provider).unregister();
        assertEquals(0, providers.size());
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.S2TopLinkContainerPersistenceUnitProvider#createEntityManagerFactory(java.lang.String)} のためのテスト・メソッド。
     */
    public void testCreateEntityManagerFactory() {
        EntityManagerFactory factory = provider.createEntityManagerFactory("persistenceUnit");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = provider.createEntityManagerFactory("persistenceUnit2");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = provider.createEntityManagerFactory("persistenceUnit3");
        assertNotNull(factory);
        assertTrue(factory instanceof EntityManagerFactoryImpl);
        factory = provider.createEntityManagerFactory("persistenceUnit4");
        assertNull(factory);
    }

}
