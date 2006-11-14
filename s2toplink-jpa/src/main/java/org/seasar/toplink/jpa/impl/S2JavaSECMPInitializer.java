package org.seasar.toplink.jpa.impl;

import java.util.Collection;
import java.util.Map;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;
import oracle.toplink.essentials.logging.AbstractSessionLog;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;

public class S2JavaSECMPInitializer extends JavaSECMPInitializer {
    
    public static JavaSECMPInitializer getJavaSECMPInitializer(String configPath, Map properties) {
        if (javaSECMPInitializer == null) {
           initializeFromContainer(configPath, properties);
        }   
        return javaSECMPInitializer;
    }
    
    public static void initializeFromContainer(String configPath, Map properties) {
        if (javaSECMPInitializer != null) {
            return;
        }
        S2Container container = S2ContainerFactory.create(configPath);
        container.init();
        try {
            javaSECMPInitializer = (JavaSECMPInitializer) container.getComponent(JavaSECMPInitializer.class);
            AbstractSessionLog.getLog().setLevel(JavaSECMPInitializer.getTopLinkLoggingLevel());
            javaSECMPInitializer.initialize(properties);
        } finally {
            container.destroy();
        }
    }
    
    private PersistenceUnitInfoFactory persistenceUnitInfoFactory;
    
    public void setPersistenceUnitInfoFactory(
            PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }

    @Override
    protected boolean callPredeploy(SEPersistenceUnitInfo persistenceUnitInfo, Map m) {
        
        persistenceUnitInfoFactory.addAutoDetectResult(persistenceUnitInfo);
        return super.callPredeploy(persistenceUnitInfo, m);
    }

    @Override
    protected ClassLoader createTempLoader(Collection col) {
        return super.createTempLoader(col);
    }
   
}
