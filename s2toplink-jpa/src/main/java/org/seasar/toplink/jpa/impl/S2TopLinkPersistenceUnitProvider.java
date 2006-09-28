package org.seasar.toplink.jpa.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.toplink.jpa.ContainerPersistence;

public class S2TopLinkPersistenceUnitProvider implements
        PersistenceUnitProvider {

    
    protected PersistenceUnitManager persistenceUnitManager;
    
    private ContainerPersistence containerPersistence;

    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }


    public void setContainerPersistence(ContainerPersistence containerPersistence) {
        this.containerPersistence = containerPersistence;
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
//        return containerPersistence.getContainerEntityManagerFactory(unitName, null);
        return Persistence.createEntityManagerFactory(unitName);
    }

}
