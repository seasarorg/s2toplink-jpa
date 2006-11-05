package org.seasar.toplink.jpa.impl;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;
import oracle.toplink.essentials.logging.AbstractSessionLog;
import oracle.toplink.essentials.logging.SessionLog;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;

public class S2JavaSECMPInitializer extends JavaSECMPInitializer {
    
    public static final String DEFAULT_AGENT_CONFIG_PATH = "s2toplink-jpa-agent.dicon";
    
//    protected Map<String, List<ClassAutoDetector>> persistenceClassAutoDetectors =
//        CollectionsUtil.newHashMap();
    
    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors =
        CollectionsUtil.newHashMap();


//    public void setPersistenceClassAutoDetector(
//            final ClassAutoDetector[] detectors) {
//        for (final ClassAutoDetector detector : detectors) {
//            addPersistenceClassAutoDetector(detector);
//        }
//    }
//
//    public void addPersistenceClassAutoDetector(final ClassAutoDetector detector) {
//        addPersistenceClassAutoDetector(null, detector);
//    }
//
//    public void addPersistenceClassAutoDetector(final String unitName,
//            final ClassAutoDetector detector) {
//        if (!persistenceClassAutoDetectors.containsKey(unitName)) {
//            persistenceClassAutoDetectors.put(unitName,
//                    new ArrayList<ClassAutoDetector>());
//        }
//        persistenceClassAutoDetectors.get(unitName).add(detector);
//    }
    
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
    
    public static JavaSECMPInitializer getJavaSECMPInitializer(Map properties) {
        if (javaSECMPInitializer == null) {
            S2JavaSECMPInitializer.initializeFromMain(properties);
        }   
        return javaSECMPInitializer;
    }
    
    protected static void initializeFromAgent(String agentArgs, Instrumentation instrumentation) throws Exception {
        AbstractSessionLog.getLog().setLevel(JavaSECMPInitializer.getTopLinkLoggingLevel());

        globalInstrumentation = instrumentation;

        String configPath = agentArgs != null ? agentArgs : DEFAULT_AGENT_CONFIG_PATH;
        S2Container container = S2ContainerFactory.create(configPath);

        try {
            javaSECMPInitializer = (JavaSECMPInitializer) container.getComponent(JavaSECMPInitializer.class);
            javaSECMPInitializer.initialize(new HashMap());
        } finally {
            container.destroy();
        }
    }
    
    public static void initializeFromMain(Map m) {
        if (javaSECMPInitializer != null) {
            return;
        }

        javaSECMPInitializer = new S2JavaSECMPInitializer();
        AbstractSessionLog.getLog().setLevel(JavaSECMPInitializer.getTopLinkLoggingLevel());

        AbstractSessionLog.getLog().log(SessionLog.FINER, "cmp_init_initialize_from_main");

        javaSECMPInitializer.initialize(m);
    }
    
    @Override
    protected boolean callPredeploy(SEPersistenceUnitInfo persistenceUnitInfo, Map m) {
        
        setMappingFiles(persistenceUnitInfo, mappingFileAutoDetectors.get(persistenceUnitInfo.getPersistenceUnitName()));
        setMappingFiles(persistenceUnitInfo, mappingFileAutoDetectors.get(null));
//        setManagedClasses(persistenceUnitInfo, persistenceClassAutoDetectors.get(persistenceUnitInfo.getPersistenceUnitName()));
//        setManagedClasses(persistenceUnitInfo, persistenceClassAutoDetectors.get(null));
        return super.callPredeploy(persistenceUnitInfo, m);
    }

    private void setMappingFiles(SEPersistenceUnitInfo persistenceUnitInfo, List<ResourceAutoDetector> autoDetectList) {
        if (autoDetectList != null) {
            for (ResourceAutoDetector rad : autoDetectList) {
                for (ResourceAutoDetector.Entry entry : rad.detect()) {
                    persistenceUnitInfo.getMappingFileNames().add(entry.getPath());
                }
            }
        }
    }
    
//    private void setManagedClasses(SEPersistenceUnitInfo persistenceUnitInfo, List<ClassAutoDetector> autoDetectList) {
//        if (autoDetectList != null) {
//            for (ClassAutoDetector cad : autoDetectList) {
//                for (Class clazz : cad.detect()) {
//                    persistenceUnitInfo.getManagedClassNames().add(clazz.getName());
//                }
//            }
//        }
//    }
    
}
