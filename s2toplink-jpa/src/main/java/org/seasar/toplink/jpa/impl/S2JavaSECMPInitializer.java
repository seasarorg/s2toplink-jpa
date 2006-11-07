package org.seasar.toplink.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;
import oracle.toplink.essentials.logging.AbstractSessionLog;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;
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
        try {
            javaSECMPInitializer = (JavaSECMPInitializer) container.getComponent(JavaSECMPInitializer.class);
            AbstractSessionLog.getLog().setLevel(JavaSECMPInitializer.getTopLinkLoggingLevel());
            javaSECMPInitializer.initialize(properties);
        } finally {
            container.destroy();
        }
    }
    
    private PersistenceUnitInfoFactory persistenceUnitInfoFactory;
    
    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors =
        CollectionsUtil.newHashMap();

    public void setPersistenceUnitInfoFactory(
            PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }

    public void setMappingFileAutoDetector(
            final ResourceAutoDetector[] resourceAutoDetectors) {
        for (final ResourceAutoDetector detector : resourceAutoDetectors) {
            addMappingFileAutoDetector(detector);
        }
    }

    public void addMappingFileAutoDetector(final ResourceAutoDetector detector) {
        addMappingFileAutoDetector(null, detector);
    }

    public void addMappingFileAutoDetector(final String unitName,
            final ResourceAutoDetector detector) {
        if (!mappingFileAutoDetectors.containsKey(unitName)) {
            mappingFileAutoDetectors.put(unitName,
                    new ArrayList<ResourceAutoDetector>());
        }
        mappingFileAutoDetectors.get(unitName).add(detector);
    }


    @Override
    protected boolean callPredeploy(SEPersistenceUnitInfo persistenceUnitInfo, Map m) {
        
        persistenceUnitInfoFactory.addAutoDetectResult(persistenceUnitInfo);
        return super.callPredeploy(persistenceUnitInfo, m);
    }
    
}
