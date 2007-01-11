/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkContainerPersistenceUnitProvider implements
        PersistenceUnitProvider {
    
    protected PersistenceUnitManager persistenceUnitManager;
    
    protected PersistenceProvider persistenceProvider;
    
    protected PersistenceUnitInfoFactory persistenceUnitInfoFactory;
    
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public void setPersistenceUnitInfoFactory(
            PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }

    @InitMethod
    public void register() {
        persistenceUnitManager.addProvider(this);
    }

    @DestroyMethod
    public void unregister() {
        persistenceUnitManager.removeProvider(this);
    }

    public EntityManagerFactory createEntityManagerFactory(String unitName) {
        
        PersistenceUnitInfo unitInfo = persistenceUnitInfoFactory.getPersistenceUnitInfo(unitName);
        if (unitInfo == null) {
            return null;
        }
        
        String providerClassName = unitInfo.getPersistenceProviderClassName();
        if (providerClassName == null
            || providerClassName.equals("")
            || providerClassName.equals(EntityManagerFactoryProvider.class.getName())
            || providerClassName.equals(oracle.toplink.essentials.PersistenceProvider.class.getName())){
        
            return persistenceProvider.createContainerEntityManagerFactory(unitInfo, new HashMap());
        }
        return null;
    }

}
