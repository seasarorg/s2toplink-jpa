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
package org.seasar.toplink.jpa;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

/**
 * PersistenceUnitInfoにsetterメソッドを追加したインターフェイス
 * @author Hidenoshin Yoshida
 *
 */
public interface S2TopLinkPersistenceUnitInfo extends PersistenceUnitInfo {

    /**
     * PersistenceUnit名を設定します。
     * @param persistenceUnitName PersistenceUnit名
     */
    public void setPersistenceUnitName(String persistenceUnitName);

    /**
     * JPAプロバイダ名を設定します。
     * @param persistenceProviderClassName JPAプロバイダ名
     */
    public void setPersistenceProviderClassName(String persistenceProviderClassName);
    
    /**
     * トランザクションのタイプを設定します。
     * @param persistenceUnitTransactionType トランザクションタイプ
     */
    public void setTransactionType(PersistenceUnitTransactionType persistenceUnitTransactionType);
    
    /**
     * JTA管理のDataSourceを設定します。
     * @param jtaDataSource JTA管理のDataSource
     */
    public void setJtaDataSource(DataSource jtaDataSource);
    
    /**
     * JTAに管理されていないDataSourceを設定します。
     * @param nonJtaDataSource JTAに管理されていないDataSource
     */
    public void setNonJtaDataSource(DataSource nonJtaDataSource);

    /**
     * Mappingファイル名のListを設定します。
     * @param mappingFiles Mappingファイル名のList
     */
    public void setMappingFileNames(List<String> mappingFiles);

    /**
     * JARファイルのURLを保持するListを設定します。
     * @param jarFileUrls JARファイルのURLを保持するList
     */
    public void setJarFileUrls(List<URL> jarFileUrls);
    
    /**
     * このPersistenceUnit情報を設定するPersistence.xmlが存在するクラスパスのルートを設定します。
     * @param persistenceUnitRootUrl ルートURL
     */
    public void setPersistenceUnitRootUrl(URL persistenceUnitRootUrl);
    
    /**
     * このPersistenceUnitで管理する対象に追加するクラス名のListを設定します。
     * @param managedClassNames クラス名のList
     */
    public void setManagedClassNames(List<String> managedClassNames);
    
    /**
     * 明示的に指定されなかったルートURL配下のEntityクラスを自動的に登録するかどうかを指定します。 
     * このメソッドは、JPAプロバイダが提供するEntityクラスの自動登録機能をコントロールするものです。
     * @param excludeUnlistedClasses Entityクラスを自動的に登録する場合true
     */
    public void setExcludeUnlistedClasses(boolean excludeUnlistedClasses);

    /**
     * JPAプロバイダ依存のプロパティを設定します。
     * @param properties Propertiesオブジェクト
     */
    public void setProperties(Properties properties);

    /**
     * クラス情報をstreamで読み込む為に一時的に利用するClassLoaderを設定します。
     * @param loader ClassLoaderオブジェクト
     */
    public void setNewTempClassLoader(ClassLoader loader);
    
    /**
     * クラスをロードする為のClassLoaderを設定します。
     * @param loader ClassLoaderオブジェクト
     */
    public void setClassLoader(ClassLoader loader);

}
