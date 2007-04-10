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
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;
import oracle.toplink.essentials.logging.AbstractSessionLog;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.jpa.PersistenceUnitConfiguration;
import org.seasar.framework.util.ChildFirstClassLoader;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.InputStreamUtil;

/**
 * TopLink EssentialsのJavaSECMPInitializerを継承したクラスです。
 * <p>
 * Seasar2が提供するEntityクラスやMappingファイルの自動登録機能に対応しています。
 * </p>
 * 
 * @author Hidenoshin Yoshida
 * 
 */
public class S2JavaSECMPInitializer extends JavaSECMPInitializer {

    /**
     * 抽象永続ユニット名のプロパティのキーです。
     */
    public static final String ABSTRACT_UNIT_NAME = "s2toplink.abstractUnitName";

    private PersistenceUnitConfiguration configuration;

    public void setPersistenceUnitConfiguration(
            final PersistenceUnitConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * JavaSECMPInitializerを取得します。
     * 
     * @param configPath
     *            JavaSECMPInitializerを生成する定義を記述したdiconファイル名
     * @param properties
     *            JavaSECMPInitializer生成時に渡すPropertiesオブジェクト
     * @return JavaSECMPInitializerオブジェクト
     */
    @SuppressWarnings("unchecked")
    public static JavaSECMPInitializer getJavaSECMPInitializer(
            final String configPath, final Map properties) {
        if (javaSECMPInitializer == null) {
            initializeFromContainer(configPath, properties);
        }
        return javaSECMPInitializer;
    }

    /**
     * JavaSECMPInitializerが生成されていない場合、configPathで指定されたdiconファイルを読み込んで生成処理を行います。
     * 
     * @param configPath
     *            JavaSECMPInitializerを生成する定義を記述したdiconファイル名
     * @param properties
     *            JavaSECMPInitializer生成時に渡すPropertiesオブジェクト
     */
    @SuppressWarnings("unchecked")
    public static void initializeFromContainer(final String configPath,
            final Map properties) {
        if (javaSECMPInitializer != null) {
            return;
        }
        final S2Container container = S2ContainerFactory.create(configPath);
        container.init();
        try {
            javaSECMPInitializer = (JavaSECMPInitializer) container
                    .getComponent(JavaSECMPInitializer.class);
            AbstractSessionLog.getLog().setLevel(
                    JavaSECMPInitializer.getTopLinkLoggingLevel());
            javaSECMPInitializer.initialize(properties);
        } finally {
            container.destroy();
        }
    }

    /**
     * 指定されたpersistenceUnitInfoにSeasar2の自動登録情報を追加し、親クラスの処理を実行します。
     * 
     * @see oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer#callPredeploy(oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo,
     *      java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    protected boolean callPredeploy(final SEPersistenceUnitInfo unitInfo,
            final Map m) {
        final String abstractUnitName = getAbstractUnitName(unitInfo);
        addMappingFiles(abstractUnitName, unitInfo);
        addPersistenceClasses(abstractUnitName, unitInfo);
        if (JavaSECMPInitializer.globalInstrumentation == null) {
            JavaSECMPInitializer.globalInstrumentation = new InstrumentationImpl(
                    unitInfo.getManagedClassNames());
        }
        return super.callPredeploy(unitInfo, m);
    }

    /**
     * 永続ユニット情報から抽象永続ユニット名を返します。
     * <p>
     * 永続ユニット情報に抽象永続ユニット名が定義されていない場合は通常の永続ユニット名を抽象永続ユニット名として返します。
     * </p>
     * 
     * @param unitInfo
     *            永続ユニット情報
     * @return 抽象永続ユニット名
     */
    protected String getAbstractUnitName(final SEPersistenceUnitInfo unitInfo) {
        final Properties props = unitInfo.getProperties();
        if (props != null && props.containsKey(ABSTRACT_UNIT_NAME)) {
            return props.getProperty(ABSTRACT_UNIT_NAME);
        }
        return unitInfo.getPersistenceUnitName();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ClassLoader createTempLoader(final Collection classNames,
            final boolean shouldOverrideLoadClassForCollectionMembers) {
        final ClassLoader currentLoader = Thread.currentThread()
                .getContextClassLoader();
        return new S2TempEntityLoader(currentLoader, classNames);
    }

    /**
     * 永続ユニット情報にSMART deploy規約に適合したマッピングファイルを自動登録します。
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void addMappingFiles(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        configuration.detectMappingFiles(abstractUnitName,
                new MappingFileHandler(unitInfo));
    }

    /**
     * 永続ユニット情報にSMART deploy規約に適合したEntityを自動登録します。
     * 
     * @param abstractUnitName
     *            抽象永続ユニット名
     * @param unitInfo
     *            永続ユニット情報
     */
    protected void addPersistenceClasses(final String abstractUnitName,
            final PersistenceUnitInfo unitInfo) {
        final ClassLoader original = Thread.currentThread()
                .getContextClassLoader();
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
     * 指定されたエンティティクラスをロードする一時的なクラスローダーです。
     * 
     * @author taedium
     * 
     */
    public static class S2TempEntityLoader extends ClassLoader {

        /**
         * このインスタンスでロードするエンティティクラス名のコレクションです。
         */
        protected Collection<String> classNames;

        /**
         * インスタンスを構築します。
         * 
         * @param parent
         *            親クラスローダー
         * @param classNames
         *            このインスタンスでロードするエンティティクラス名の集合
         */
        protected S2TempEntityLoader(final ClassLoader parent,
                final Collection<String> classNames) {
            super(parent);
            this.classNames = classNames;
        }

        @Override
        public Enumeration<URL> getResources(String name)
                throws java.io.IOException {
            return this.getParent().getResources(name);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            if (classNames.contains(name)) {
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    c = findClass(name);
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            } else {
                return super.loadClass(name, resolve);
            }
        }

        @Override
        protected Class<?> findClass(final String name)
                throws ClassNotFoundException {
            final String path = ClassUtil.getResourcePath(name);
            final InputStream in = getResourceAsStream(path);
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            final byte[] bytes = InputStreamUtil.getBytes(in);
            return defineClass(name, bytes, 0, bytes.length);
        }
    }
}
