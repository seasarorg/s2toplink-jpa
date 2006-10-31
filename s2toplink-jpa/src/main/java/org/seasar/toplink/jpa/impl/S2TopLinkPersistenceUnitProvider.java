package org.seasar.toplink.jpa.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;

public class S2TopLinkPersistenceUnitProvider implements
        PersistenceUnitProvider {

    
    protected PersistenceUnitManager persistenceUnitManager;
    
    protected PersistenceProvider persistenceProvider;
    
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
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
        return persistenceProvider.createEntityManagerFactory(unitName, null);
    }

}
