package org.seasar.toplink.jpa.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;

public class S2TopLinkPersistenceUnitProvider implements
        PersistenceUnitProvider {

    
    protected PersistenceUnitManager persistenceUnitManager;
    
    protected PersistenceProvider persistenceProvider;
    
    protected S2JavaSECMPInitializer s2JavaSECMPInitializer;
    
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public void setS2JavaSECMPInitializer(S2JavaSECMPInitializer s2JavaSECMPInitializer) {
        this.s2JavaSECMPInitializer = s2JavaSECMPInitializer;
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
        PersistenceUnitInfo unitInfo = s2JavaSECMPInitializer.getPersistenceUnitInfo(unitName);
        if (checkPersistenceProvider(unitInfo)) {
            return persistenceProvider.createEntityManagerFactory(unitName, null);
        } else {
            return null;
        }
    }
    
    protected boolean checkPersistenceProvider(PersistenceUnitInfo unitInfo) {
        if (unitInfo == null) {
            return false;
        }
        String persistenceProviderName = unitInfo.getPersistenceProviderClassName();
        return persistenceProviderName == null
            || EntityManagerFactoryProvider.class.getName().equals(persistenceProviderName)
            || oracle.toplink.essentials.PersistenceProvider.class.getName().equals(persistenceProviderName)
            || "".equals(persistenceProviderName);
    }

}
