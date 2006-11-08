package org.seasar.toplink.jpa.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.persistence.Archive;
import oracle.toplink.essentials.ejb.cmp3.persistence.PersistenceUnitProcessor;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;
import org.seasar.toplink.jpa.AutoDetectorFactory;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;

public class PersistenceUnitInfoFactoryImpl implements
        PersistenceUnitInfoFactory {

    protected Map<String, SEPersistenceUnitInfo> sePersistenceUnitInfoMap;

    protected S2Container container;
    
    private AutoDetectorFactory autoDetectorFactory;
    
    protected void init() {
        sePersistenceUnitInfoMap = new HashMap<String, SEPersistenceUnitInfo>();
        Set<Archive> archives = PersistenceUnitProcessor.findPersistenceArchives();
        for (Archive archive : archives) {
            List<SEPersistenceUnitInfo> unitInfoList = PersistenceUnitProcessor.getPersistenceUnits(archive, Thread.currentThread().getContextClassLoader());
            for (SEPersistenceUnitInfo unitInfo : unitInfoList) {
                sePersistenceUnitInfoMap.put(unitInfo.getPersistenceUnitName(), unitInfo);
            }
            
        }
    }
    
    public void setContainer(S2Container container) {
        this.container = container;
    }

    public void setAutoDetectorFactory(AutoDetectorFactory autoDetectorFactory) {
        this.autoDetectorFactory = autoDetectorFactory;
    }

    public PersistenceUnitInfo getPersistenceUnitInfo(String unitName) {
        if (sePersistenceUnitInfoMap == null) {
            init();
        }
        SEPersistenceUnitInfo unitInfo = sePersistenceUnitInfoMap.get(unitName);
        if (unitInfo == null) {
            return null;
        }
        S2TopLinkPersistenceUnitInfo s2UnitInfo =
            S2TopLinkPersistenceUnitInfo.class.cast(container.getComponent(S2TopLinkPersistenceUnitInfo.class));
        s2UnitInfo.setPersistenceUnitName(unitInfo.getPersistenceUnitName());
        s2UnitInfo.setPersistenceProviderClassName(unitInfo.getPersistenceProviderClassName());
        s2UnitInfo.setTransactionType(unitInfo.getTransactionType());
        s2UnitInfo.setJtaDataSource(unitInfo.getJtaDataSource());
        s2UnitInfo.setNonJtaDataSource(unitInfo.getNonJtaDataSource());
        s2UnitInfo.setMappingFileNames(unitInfo.getMappingFileNames());
        s2UnitInfo.setJarFileUrls(unitInfo.getJarFileUrls());
        s2UnitInfo.setPersistenceUnitRootUrl(unitInfo.getPersistenceUnitRootUrl());
        s2UnitInfo.setManagedClassNames(unitInfo.getManagedClassNames());
        s2UnitInfo.setExcludeUnlistedClasses(unitInfo.excludeUnlistedClasses());
        s2UnitInfo.setProperties(unitInfo.getProperties());
        
        setMappingFiles(s2UnitInfo);
        return s2UnitInfo;
    }

    private void setMappingFiles(PersistenceUnitInfo unitInfo) {
        setMappingFiles(unitInfo, autoDetectorFactory.getResourceAutoDetectorList(null));
        if (!StringUtil.isEmpty(unitInfo.getPersistenceUnitName())) {
            setMappingFiles(unitInfo, autoDetectorFactory.getResourceAutoDetectorList(unitInfo.getPersistenceUnitName()));            
        }
    }
    
    private void setMappingFiles(final PersistenceUnitInfo persistenceUnitInfo, List<ResourceAutoDetector> autoDetectList) {
        if (autoDetectList != null) {
            for (ResourceAutoDetector rad : autoDetectList) {
                rad.detect(new ResourceAutoDetector.ResourceHandler() {

                    public void processResource(String path, InputStream is) {
                        persistenceUnitInfo.getMappingFileNames().add(path);
                    }
                    
                });
            }
        }
    }

    public void addAutoDetectResult(PersistenceUnitInfo persistenceUnitInfo) {
        setMappingFiles(persistenceUnitInfo);
    }

}
