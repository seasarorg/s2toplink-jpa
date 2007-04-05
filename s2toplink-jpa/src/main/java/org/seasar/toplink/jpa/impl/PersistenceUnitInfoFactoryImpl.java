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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.toplink.jpa.PersistenceUnitInfoFactory;
import org.seasar.toplink.jpa.S2TopLinkConfiguration;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class PersistenceUnitInfoFactoryImpl implements
        PersistenceUnitInfoFactory {

    private static final Logger logger = Logger
            .getLogger(PersistenceUnitInfoFactoryImpl.class);

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
    protected S2TopLinkConfiguration s2TopLinkConfiguration;

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
    public void setS2TopLinkConfiguration(
            S2TopLinkConfiguration topLinkConfiguration) {
        s2TopLinkConfiguration = topLinkConfiguration;
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

        if (s2TopLinkConfiguration != null) {
            addMappingFiles(abstractUnitName, s2UnitInfo);
            addAnnotatedClasses(abstractUnitName, s2UnitInfo);
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
        s2TopLinkConfiguration.detectMappingFiles(abstractUnitName,
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
    protected void addAnnotatedClasses(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        s2TopLinkConfiguration.detectPersistenceClasses(abstractUnitName,
                new PersistenceClassHandler(unitInfo));
    }

    /**
     * Mappingファイル自動登録用ResourceHandler
     * 
     * @author Hidenoshin Yoshida
     * 
     */
    public class MappingFileHandler implements ResourceHandler {

        /**
         * PersistenceUnitInfo
         */
        protected PersistenceUnitInfo unitInfo;

        /**
         * コンストラクタ
         * 
         * @param unitInfo
         *            PersistenceUnitInfo
         */
        public MappingFileHandler(final PersistenceUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        /**
         * @see org.seasar.framework.util.ResourceTraversal.ResourceHandler#processResource(java.lang.String,
         *      java.io.InputStream)
         */
        public void processResource(final String path, final InputStream is) {
            if (logger.isDebugEnabled()) {
                logger.log("DTLJPA0002", new Object[] { path,
                        unitInfo.getPersistenceUnitName() });
            }
            unitInfo.getMappingFileNames().add(path);
        }

    }

    /**
     * Entity自動登録用ClassHandler
     * 
     * @author Hidenoshin Yoshida
     * 
     */
    public class PersistenceClassHandler implements ClassHandler {

        /**
         * PersistenceUnitInfo
         */
        protected PersistenceUnitInfo unitInfo;

        /**
         * コンストラクタ
         * 
         * @param unitInfo
         *            PersistenceUnitInfo
         */
        public PersistenceClassHandler(final PersistenceUnitInfo unitInfo) {
            this.unitInfo = unitInfo;
        }

        /**
         * @see org.seasar.framework.util.ClassTraversal.ClassHandler#processClass(java.lang.String,
         *      java.lang.String)
         */
        public void processClass(final String packageName,
                final String shortClassName) {
            final String className = ClassUtil.concatName(packageName,
                    shortClassName);
            if (logger.isDebugEnabled()) {
                logger.log("DTLJPA0001", new Object[] { className,
                        unitInfo.getPersistenceUnitName() });
            }
            unitInfo.getManagedClassNames().add(className);
        }
    }

    /**
     * @see org.seasar.toplink.jpa.PersistenceUnitInfoFactory#addAutoDetectResult(String,
     *      PersistenceUnitInfo)
     */
    public void addAutoDetectResult(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        if (s2TopLinkConfiguration != null) {
            addMappingFiles(abstractUnitName, unitInfo);
            addAnnotatedClasses(abstractUnitName, unitInfo);
        }
    }

}
