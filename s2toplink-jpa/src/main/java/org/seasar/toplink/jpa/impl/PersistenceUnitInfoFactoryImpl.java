/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceTraversal;
import org.seasar.framework.util.StringUtil;
import org.seasar.toplink.jpa.AutoDetectorFactory;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class PersistenceUnitInfoFactoryImpl implements
        PersistenceUnitInfoFactory {

    /**
     * PersistenceUnit名をキー、SEPersistenceUnitInfoを値に持つMap
     */
    protected Map<String, SEPersistenceUnitInfo> sePersistenceUnitInfoMap;

    /**
     * S2コンテナ
     */
    protected S2Container container;
    
    /**
     * Entityクラス、Mappingファイルの自動登録AutoDetectorのファクトリ
     */
    protected AutoDetectorFactory autoDetectorFactory;
    
    /**
     * 初期化処理を行います。
     * クラスパス上のpersistence.xmlを検索し、設定データをsePersistenceUnitInfoMapに保持します。
     */
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
    
    /**
     * S2コンテナを設定します。
     * @param container 設定するS2コンテナ
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * AutoDetectorFactoryを設定します。
     * @param autoDetectorFactory 設定するAutoDetectorFactory
     */
    public void setAutoDetectorFactory(AutoDetectorFactory autoDetectorFactory) {
        this.autoDetectorFactory = autoDetectorFactory;
    }

    /**
     * @see org.seasar.toplink.jpa.PersistenceUnitInfoFactory#getPersistenceUnitInfo(java.lang.String)
     */
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
        setPersistenceClasses(s2UnitInfo);
        return s2UnitInfo;
    }

    /**
     * 指定されたPersistenceUnitInfoオブジェクトに自動登録対象のMappingファイル情報を追加します。
     * @param unitInfo PersistenceUnitInfoオブジェクト
     */
    protected void setMappingFiles(PersistenceUnitInfo unitInfo) {
        setMappingFiles(unitInfo, null);
        if (!StringUtil.isEmpty(unitInfo.getPersistenceUnitName())) {
            setMappingFiles(unitInfo, unitInfo.getPersistenceUnitName());            
        }
    }
    
    /**
     * 指定されたPersistenceUnitInfoオブジェクトに対して、unitNameで登録されている、自動登録対象のMappingファイル情報を追加します。
     * @param unitInfo PersistenceUnitInfoオブジェクト
     * @param unitName PersistenceUnit名
     */
    protected void setMappingFiles(final PersistenceUnitInfo unitInfo, String unitName) {
        List<ResourceAutoDetector> autoDetectList = autoDetectorFactory.getResourceAutoDetectorList(unitName);
        if (autoDetectList != null) {
            for (ResourceAutoDetector rad : autoDetectList) {
                rad.detect(new ResourceTraversal.ResourceHandler() {

                    public void processResource(String path, InputStream is) {
                        unitInfo.getMappingFileNames().add(path);
                    }
                    
                });
            }
        }
    }
    
    /**
     * 指定されたPersistenceUnitInfoオブジェクトに自動登録対象のEntityクラス情報を追加します。
     * @param unitInfo PersistenceUnitInfoオブジェクト
     */
    protected void setPersistenceClasses(PersistenceUnitInfo unitInfo) {
        setPersistenceClasses(unitInfo, null);
        if (!StringUtil.isEmpty(unitInfo.getPersistenceUnitName())) {
            setPersistenceClasses(unitInfo, unitInfo.getPersistenceUnitName());
        }
    }
    
    /**
     * 指定されたPersistenceUnitInfoオブジェクトに対して、unitNameで登録されている、自動登録対象のEntityクラス情報を追加します。
     * @param unitInfo PersistenceUnitInfoオブジェクト
     * @param unitName PersistenceUnit名
     */
    protected void setPersistenceClasses(final PersistenceUnitInfo unitInfo, String unitName) {
        List<ClassAutoDetector> autoDetectList = autoDetectorFactory.getClassAutoDetectorList(unitName);
        if (autoDetectList != null) {
            for (ClassAutoDetector cad : autoDetectList) {
                cad.detect(new ClassTraversal.ClassHandler() {

                    public void processClass(String packageName, String shortClassName) {
                        unitInfo.getManagedClassNames().add(ClassUtil.concatName(packageName, shortClassName));
                        
                    }
                    
                });
            }
        }
    }

    /**
     * @see org.seasar.toplink.jpa.PersistenceUnitInfoFactory#addAutoDetectResult(javax.persistence.spi.PersistenceUnitInfo)
     */
    public void addAutoDetectResult(PersistenceUnitInfo unitInfo) {
        setMappingFiles(unitInfo);
        setPersistenceClasses(unitInfo);
    }

}
