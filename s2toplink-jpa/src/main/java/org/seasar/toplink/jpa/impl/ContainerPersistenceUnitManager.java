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

public class ContainerPersistenceUnitManager extends PersistenceUnitManager {

    private final Map<String, EntityManagerFactory> persistenceUnits = new ConcurrentHashMap<String, EntityManagerFactory>();

    private final Map<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>> emfContexts = new HashMap<EntityManagerFactory, ConcurrentMap<Transaction, EntityManager>>();
    
    private final Map<String, Map> persistenceUnitProperties = new ConcurrentHashMap<String, Map>();

    private ContainerPersistence containerPersistence;
    
    public void setContainerPersistence(
		ContainerPersistence containerPersistence) {
		this.containerPersistence = containerPersistence;
	}
    
    public void addPersistenceUnitProperties(String persistenceUnitName, Map persistenceUnitProperties) {
        this.persistenceUnitProperties.put(persistenceUnitName, persistenceUnitProperties);
    }

	@DestroyMethod
    public void close() {
        for (final Map.Entry<String, EntityManagerFactory> entry : persistenceUnits.entrySet()) {
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
        return createEntityManagerFactory(unitName);
    }

    EntityManagerFactory createEntityManagerFactory(final String unitName) {
        try {
			final EntityManagerFactory emf =
                containerPersistence.getContainerEntityManagerFactory(unitName, persistenceUnitProperties.get(unitName));
			
			persistenceUnits.put(unitName, emf);
			emfContexts.put(emf, new ConcurrentHashMap<Transaction, EntityManager>());
			return emf;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
    }

    public ConcurrentMap<Transaction, EntityManager> getEmfContext(
            final EntityManagerFactory emf) {
        return emfContexts.get(emf);
    }

}
