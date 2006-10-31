package org.seasar.toplink.jpa.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;
import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerSetupImpl;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

public class S2EntityManagerFactoryProvider extends
        EntityManagerFactoryProvider {
    
    protected JavaSECMPInitializer javaSECMPInitializer;

    public void setJavaSECMPInitializer(JavaSECMPInitializer javaSECMPInitializer) {
        this.javaSECMPInitializer = javaSECMPInitializer;
    }

    @Override
    public EntityManagerFactory createEntityManagerFactory(String emName, Map properties) {
        Map nonNullProperties = (properties == null) ? new HashMap() : properties;
        String name = emName;
        if (name == null){
            name = "";
        }
        JavaSECMPInitializer initializer = javaSECMPInitializer;
//        JavaSECMPInitializer initializer = S2JavaSECMPInitializer.getJavaSECMPInitializer(nonNullProperties);
        EntityManagerSetupImpl emSetupImpl = initializer.getEntityManagerSetupImpl(name);
        if(emSetupImpl.isUndeployed()) {
            ((SEPersistenceUnitInfo)emSetupImpl.getPersistenceUnitInfo()).setClassLoader(JavaSECMPInitializer.getMainLoader());
            ((SEPersistenceUnitInfo)emSetupImpl.getPersistenceUnitInfo()).setNewTempClassLoader(JavaSECMPInitializer.getMainLoader());
            // the first parameter is ignored if predeploy called after undeploy
            emSetupImpl.predeploy(null, nonNullProperties);
        }
        
        EntityManagerFactoryImpl factory = new EntityManagerFactoryImpl(emSetupImpl, nonNullProperties);

        // This code has been added to allow validation to occur without actually calling createEntityManager
        if (emSetupImpl.shouldGetSessionOnCreateFactory(nonNullProperties)) {
            factory.getServerSession();
        }
        return factory;
    }

    
}
