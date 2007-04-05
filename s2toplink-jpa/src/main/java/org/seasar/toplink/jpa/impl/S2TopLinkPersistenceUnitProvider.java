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

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;

/**
 * TopLink Essentials用のPersistenceUnitProvier実装です。
 * PersistenceProviderのcreateEntityManagerFactoryメソッドを利用して、EntityManagerFactoryを作成します。
 * javaagentからS2JavaSECMPInitializerAgentを実行していた場合、N:1の関連に対するEntityのLAZYロード設定が有効になります。
 * 
 * @author Hidenoshin Yoshida
 */
public class S2TopLinkPersistenceUnitProvider implements
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
     * JavaSECMPInitializerオブジェクト
     */
    protected JavaSECMPInitializer javaSECMPInitializer;

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
     * JavaSECMPInitializerを設定します。
     * 
     * @param javaSECMPInitializer
     *            設定するJavaSECMPInitializer
     */
    public void setJavaSECMPInitializer(
            JavaSECMPInitializer javaSECMPInitializer) {
        this.javaSECMPInitializer = javaSECMPInitializer;
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
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(java.lang.String)
     */
    public EntityManagerFactory createEntityManagerFactory(String unitName) {
        return createEntityManagerFactory(null, unitName);
    }

    /**
     * @see org.seasar.framework.jpa.PersistenceUnitProvider#createEntityManagerFactory(String,
     *      String)
     */
    public EntityManagerFactory createEntityManagerFactory(
            String abstractUnitName, String concreteUnitName) {
        return persistenceProvider.createEntityManagerFactory(concreteUnitName,
                null);
    }

}
