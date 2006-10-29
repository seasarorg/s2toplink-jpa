package org.seasar.toplink.jpa.impl;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;

public class S2TopLinkPersistenceUnitProvider implements
        PersistenceUnitProvider {

    
    protected PersistenceUnitManager persistenceUnitManager;
    
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
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
        EntityManagerFactoryProvider provider = new EntityManagerFactoryProvider();
        return provider.createEntityManagerFactory(unitName, null);
    }

}
