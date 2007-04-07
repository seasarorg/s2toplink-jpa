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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.persistence.Archive;
import oracle.toplink.essentials.ejb.cmp3.persistence.PersistenceUnitProcessor;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.util.ChildFirstClassLoader;
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
     * Entity・マッピングファイル自動登録用Configuration
     */
    protected PersistenceUnitConfiguration configuration;

    /**
     * 初期化処理を行います。
     * クラスパス上のpersistence.xmlを検索し、設定データをsePersistenceUnitInfoMapに保持します。
     */
    protected void init() {
        sePersistenceUnitInfoMap = new HashMap<String, SEPersistenceUnitInfo>();
        Set<Archive> archives = PersistenceUnitProcessor
                .findPersistenceArchives();
        for (Archive archive : archives) {
            List<SEPersistenceUnitInfo> unitInfoList = PersistenceUnitProcessor
                    .getPersistenceUnits(archive, Thread.currentThread()
                            .getContextClassLoader());
            for (SEPersistenceUnitInfo unitInfo : unitInfoList) {
                sePersistenceUnitInfoMap.put(unitInfo.getPersistenceUnitName(),
                        unitInfo);
            }

        }
    }

    /**
     * S2コンテナを設定します。
     * 
     * @param container
     *            設定するS2コンテナ
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * Configurationを設定します
     * 
     * @param topLinkConfiguration
     *            設定するConfiguration
     */
    @Binding(bindingType = BindingType.MAY)
    public void setPersistenceUnitConfiguration(
            PersistenceUnitConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @see org.seasar.toplink.jpa.PersistenceUnitInfoFactory#getPersistenceUnitInfo(String,
     *      String)
     */
    public PersistenceUnitInfo getPersistenceUnitInfo(String abstractUnitName,
            String concreteUnitName) {
        if (sePersistenceUnitInfoMap == null) {
            init();
        }
        SEPersistenceUnitInfo unitInfo = sePersistenceUnitInfoMap
                .get(concreteUnitName);
        if (unitInfo == null) {
            return null;
        }
        S2TopLinkPersistenceUnitInfo s2UnitInfo = S2TopLinkPersistenceUnitInfo.class
                .cast(container
                        .getComponent(S2TopLinkPersistenceUnitInfo.class));
        s2UnitInfo.setPersistenceUnitName(unitInfo.getPersistenceUnitName());
        s2UnitInfo.setPersistenceProviderClassName(unitInfo
                .getPersistenceProviderClassName());
        s2UnitInfo.setTransactionType(unitInfo.getTransactionType());
        s2UnitInfo.setJtaDataSource(unitInfo.getJtaDataSource());
        s2UnitInfo.setNonJtaDataSource(unitInfo.getNonJtaDataSource());
        s2UnitInfo.setMappingFileNames(unitInfo.getMappingFileNames());
        s2UnitInfo.setJarFileUrls(unitInfo.getJarFileUrls());
        s2UnitInfo.setPersistenceUnitRootUrl(unitInfo
                .getPersistenceUnitRootUrl());
        s2UnitInfo.setManagedClassNames(unitInfo.getManagedClassNames());
        s2UnitInfo.setExcludeUnlistedClasses(unitInfo.excludeUnlistedClasses());
        s2UnitInfo.setProperties(unitInfo.getProperties());

        if (configuration != null) {
            addMappingFiles(abstractUnitName, s2UnitInfo);
            addPersistenceClasses(abstractUnitName, s2UnitInfo);
        }
        return s2UnitInfo;
    }

    /**
     * PersistenceUnitInfoにSmart Deploy規約に適合したマッピングファイルを自動登録します
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            PersistenceUnitInfo
     */
    protected void addMappingFiles(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        configuration.detectMappingFiles(abstractUnitName,
                new MappingFileHandler(unitInfo));
    }

    /**
     * PersistenceUnitInfoにSmart Deploy規約に適合したEntityを自動登録します
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            PersistenceUnitInfo
     */
    protected void addPersistenceClasses(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(
                new ChildFirstClassLoader(original));
        try {

            configuration.detectPersistenceClasses(abstractUnitName,
                    new PersistenceClassHandler(unitInfo));
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
