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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import oracle.toplink.essentials.ejb.cmp3.EntityManagerFactoryProvider;
import oracle.toplink.essentials.ejb.cmp3.persistence.PersistenceUnitProcessor;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.jpa.PersistenceUnitInfoFactory;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.ChildFirstClassLoader;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

/**
 * TopLink Essentials用のPersistenceUnitProvier実装です。
 * PersistenceProviderのcreateContainerEntityManagerFactoryメソッドを利用して、EntityManagerFactoryを作成します。
 * javaagentは利用せず、EntityManagerFactory生成処理の中でEntityクラスのエンハンス処理を実行します。
 * 
 * @author Hidenoshin Yoshida
 */
public class S2TopLinkContainerPersistenceUnitProvider implements
        PersistenceUnitProvider {

    /**
     * PersistenceUnitManagerオブジェクト
     */
    protected PersistenceUnitManager persistenceUnitManager;

    /**
     * PersistenceProviderオブジェクト
     */
    protected PersistenceProvider persistenceProvider;

    /**
     * PersistenceUnitInfoのファクトリオブジェクト
     */
    protected PersistenceUnitInfoFactory persistenceUnitInfoFactory;
    
    /**
     * Entity・マッピングファイル自動登録用Configuration
     */
    protected PersistenceUnitConfiguration configuration;

    /**
     * Entityのエンハンス処理を行うオブジェクト
     */
    protected JpaInstrumentation jpaInstrumentation;

    /**
     * PersistenceUnitManagerを設定します。
     * 
     * @param persistenceUnitManager
     *            設定するPersistenceUnitManager
     */
    public void setPersistenceUnitManager(
            PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    /**
     * PersistenceProviderを設定します。
     * 
     * @param persistenceProvider
     *            設定するPersistenceProvider
     */
    public void setPersistenceProvider(PersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }
    
    /**
     * PersisteceUnitConfigurationを設定します。
     * @param configuration 設定する configuration
     */
    public void setConfiguration(PersistenceUnitConfiguration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * PersistenceUnitInfoFactoryを設定します。
     * @param persistenceUnitInfoFactory 設定する persistenceUnitInfoFactory
     */
    public void setPersistenceUnitInfoFactory(
            PersistenceUnitInfoFactory persistenceUnitInfoFactory) {
        this.persistenceUnitInfoFactory = persistenceUnitInfoFactory;
    }
    
    /**
     * JpaInstrumentationを設定します。
     * @param jpaInstrumentation 設定する jpaInstrumentation
     */
    public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
        this.jpaInstrumentation = jpaInstrumentation;
    }
    
    /**
     * PersistenceUnitManagerにこのオブジェクトを登録します。
     */
    @InitMethod
    public void register() {
        persistenceUnitManager.addProvider(this);
    }

    /**
     * PersistenceUnitManagerからこのオブジェクトを削除します。
     */
    @DestroyMethod
    public void unregister() {
        persistenceUnitManager.removeProvider(this);
    }
    
    /**
     * PersistenceUnitNameに対応するPersistenceUnitInfoを生成して返します。
     * @param unitName PersistenceUnit名
     * @return unitNameに対応するPersistenceUnitInfo
     */
    public PersistenceUnitInfo getPersistenceUnitInfo(String unitName) {
        unitName = unitName == null ? "" : unitName;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources("META-INF/persistence.xml");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                List<PersistenceUnitInfo> infoList = persistenceUnitInfoFactory.createPersistenceUnitInfo(url);
                for (PersistenceUnitInfo unitInfo : infoList) {
                    String unitInfoName = unitInfo.getPersistenceUnitName();
                    unitInfoName = unitInfoName == null ? "" : unitInfoName;
                    if (unitName.equals(unitInfoName)) {
                        return unitInfo;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
    
    /**
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(java.lang.String)
     */
    public EntityManagerFactory createEntityManagerFactory(String unitName) {
        return createEntityManagerFactory(unitName, unitName);
    }

    /**
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(String,
     *      String)
     */
    public EntityManagerFactory createEntityManagerFactory(
            String abstractUnitName, String concreteUnitName) {

        PersistenceUnitInfo unitInfo = getPersistenceUnitInfo(concreteUnitName);
        if (unitInfo == null) {
            return null;
        }
        
        String providerClassName = unitInfo.getPersistenceProviderClassName();
        if (providerClassName == null
                || providerClassName.equals("")
                || providerClassName.equals(EntityManagerFactoryProvider.class
                        .getName())
                || providerClassName
                        .equals(oracle.toplink.essentials.PersistenceProvider.class
                                .getName())) {

            if (configuration != null) {
                addMappingFiles(abstractUnitName, unitInfo);
                addPersistenceClasses(abstractUnitName, unitInfo);
            }
            return persistenceProvider.createContainerEntityManagerFactory(
                    new TopLinkPersistenceUnitInfo(unitInfo), new HashMap<Object, Object>());
        }
        return null;
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
    
    /**
     * TopLink Essentials独自の処理を行うPersistenceUnitInfo実装
     * @author hidenoshin
     *
     */
    public class TopLinkPersistenceUnitInfo implements PersistenceUnitInfo {
        
        private PersistenceUnitInfo originalUnitInfo;

        /**
         * オリジナルPersistenceUnitInfoを受け取ってインスタンスを生成します。
         * @param originalUnitInfo オリジナルのPersistenceUnitInfo
         */
        public TopLinkPersistenceUnitInfo(PersistenceUnitInfo originalUnitInfo) {
            this.originalUnitInfo = originalUnitInfo;
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#addTransformer(javax.persistence.spi.ClassTransformer)
         */
        public void addTransformer(ClassTransformer transformer) {
            originalUnitInfo.addTransformer(transformer);
            jpaInstrumentation.addTransformer(transformer, getClassLoader());
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#excludeUnlistedClasses()
         */
        public boolean excludeUnlistedClasses() {
            return originalUnitInfo.excludeUnlistedClasses();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getClassLoader()
         */
        public ClassLoader getClassLoader() {
            return originalUnitInfo.getClassLoader();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getJarFileUrls()
         */
        public List<URL> getJarFileUrls() {
            return originalUnitInfo.getJarFileUrls();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getJtaDataSource()
         */
        public DataSource getJtaDataSource() {
            return originalUnitInfo.getJtaDataSource();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getManagedClassNames()
         */
        public List<String> getManagedClassNames() {
            return originalUnitInfo.getManagedClassNames();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getMappingFileNames()
         */
        public List<String> getMappingFileNames() {
            return originalUnitInfo.getMappingFileNames();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()
         */
        public ClassLoader getNewTempClassLoader() {
            Set<String> set = null;
            if (excludeUnlistedClasses()) {
                set = CollectionsUtil.newHashSet(getManagedClassNames());
            } else {
                set = PersistenceUnitProcessor.buildClassSet(this, getClassLoader());
            }
            return new S2TopLinkTempClassLoader(getClassLoader(), set);
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getNonJtaDataSource()
         */
        public DataSource getNonJtaDataSource() {
            return originalUnitInfo.getNonJtaDataSource();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getPersistenceProviderClassName()
         */
        public String getPersistenceProviderClassName() {
            return originalUnitInfo.getPersistenceProviderClassName();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getPersistenceUnitName()
         */
        public String getPersistenceUnitName() {
            return originalUnitInfo.getPersistenceUnitName();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getPersistenceUnitRootUrl()
         */
        public URL getPersistenceUnitRootUrl() {
            return originalUnitInfo.getPersistenceUnitRootUrl();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getProperties()
         */
        public Properties getProperties() {
            return originalUnitInfo.getProperties();
        }

        /**
         * @see javax.persistence.spi.PersistenceUnitInfo#getTransactionType()
         */
        public PersistenceUnitTransactionType getTransactionType() {
            return originalUnitInfo.getTransactionType();
        }        
        
    }
}
