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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transaction;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.toplink.jpa.ContainerPersistence;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class ContainerPersistenceUnitManager implements PersistenceUnitManager {

    private final Map<String, EntityManagerFactory> persistenceUnits = new HashMap<String, EntityManagerFactory>();

    private final Map<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>> emfContexts = new HashMap<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>>();

    private final Map<String, Map> persistenceUnitProperties = new HashMap<String, Map>();

    private ContainerPersistence containerPersistence;

    public void setContainerPersistence(
            ContainerPersistence containerPersistence) {
        this.containerPersistence = containerPersistence;
    }

    public void addPersistenceUnitProperties(String persistenceUnitName,
            Map persistenceUnitProperties) {
        this.persistenceUnitProperties.put(persistenceUnitName,
                persistenceUnitProperties);
    }

    @DestroyMethod
    public void close() {
        for (final Map.Entry<String, EntityManagerFactory> entry : persistenceUnits
                .entrySet()) {
            entry.getValue().close();
        }
        persistenceUnits.clear();
        emfContexts.clear();
        persistenceUnitProperties.clear();
    }

    public synchronized EntityManagerFactory getEntityManagerFactory(
            final String unitName) {
        final EntityManagerFactory emf = persistenceUnits.get(unitName);
        if (emf != null) {
            return emf;
        }
        return createContainerEntityManagerFactory(unitName);
    }

    protected EntityManagerFactory createContainerEntityManagerFactory(
            final String unitName) {
        final EntityManagerFactory emf = containerPersistence
                .getContainerEntityManagerFactory(unitName,
                        persistenceUnitProperties.get(unitName));

        persistenceUnits.put(unitName, emf);
        emfContexts.put(emf,
                new ConcurrentHashMap<Transaction, EntityManager>());
        return emf;
    }

    public ConcurrentMap<Transaction, EntityManager> getEmfContext(
            final EntityManagerFactory emf) {
        return emfContexts.get(emf);
    }

}
