package org.seasar.toplink.jpa.impl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerSetupImpl;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;

public class S2TopLinkPersistenceUnitProvider implements
        PersistenceUnitProvider {

    
    protected PersistenceUnitManager persistenceUnitManager;
    
    protected PersistenceProvider persistenceProvider;
    
    protected JavaSECMPInitializer javaSECMPInitializer;
    
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    public void setJavaSECMPInitializer(JavaSECMPInitializer javaSECMPInitializer) {
        this.javaSECMPInitializer = javaSECMPInitializer;
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
        EntityManagerSetupImpl setUp = javaSECMPInitializer.getEntityManagerSetupImpl(unitName);
        if (setUp == null) {
            return null;
        }
        return persistenceProvider.createEntityManagerFactory(unitName, null);
    }
}
