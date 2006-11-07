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
            || providerClassName.equals(PersistenceProvider.class.getName())){
        
            return persistenceProvider.createContainerEntityManagerFactory(unitInfo, new HashMap());
        }
        return null;
    }

}
