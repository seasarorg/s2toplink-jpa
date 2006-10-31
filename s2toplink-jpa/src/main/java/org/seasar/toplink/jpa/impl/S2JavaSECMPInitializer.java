package org.seasar.toplink.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.tiger.CollectionsUtil;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;
import oracle.toplink.essentials.logging.AbstractSessionLog;
import oracle.toplink.essentials.logging.SessionLog;

public class S2JavaSECMPInitializer extends JavaSECMPInitializer {
    
    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors = CollectionsUtil
    .newHashMap();

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
    
    public S2JavaSECMPInitializer() {
        javaSECMPInitializer = this;
        AbstractSessionLog.getLog().setLevel(JavaSECMPInitializer.getTopLinkLoggingLevel());
        AbstractSessionLog.getLog().log(SessionLog.FINER, "cmp_init_initialize_from_main");
    }

    @Override
    protected boolean callPredeploy(SEPersistenceUnitInfo persistenceUnitInfo, Map m) {
        
        List<ResourceAutoDetector> mapFileList = mappingFileAutoDetectors.get(persistenceUnitInfo.getPersistenceUnitName());
        List<ResourceAutoDetector> mapFileList2 = mappingFileAutoDetectors.get(null);
        if (mapFileList == null) {
            mapFileList =  mapFileList2;
        } else if (mapFileList2 != null){
            mapFileList.addAll(mapFileList2);
        }
        if (mapFileList != null) {
            for (ResourceAutoDetector rad : mapFileList) {
                for (ResourceAutoDetector.Entry entry : rad.detect()) {
                    persistenceUnitInfo.getMappingFileNames().add(entry.getPath());
                }
            }
        }
        return super.callPredeploy(persistenceUnitInfo, m);
    }


}
