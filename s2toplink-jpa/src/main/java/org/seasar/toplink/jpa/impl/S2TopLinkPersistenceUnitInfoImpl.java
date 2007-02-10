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

import java.util.Set;

import javax.persistence.spi.ClassTransformer;
import javax.sql.DataSource;

import oracle.toplink.essentials.ejb.cmp3.persistence.PersistenceUnitProcessor;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

import org.seasar.extension.datasource.impl.SingletonDataSourceProxy;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.toplink.jpa.JpaInstrumentation;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;

/**
 * @author Hidenoshin Yoshida
 */
public class S2TopLinkPersistenceUnitInfoImpl extends SEPersistenceUnitInfo
        implements S2TopLinkPersistenceUnitInfo {

    /**
     * エンハンス対象Entityクラス名のSet
     */
    protected Set<String> tempClassNameSet;

    /**
     * Jpainstrumentationオブジェクト
     */
    protected JpaInstrumentation jpaInstrumentation;

    /**
     * NamingConventionオブジェクト
     */
    protected NamingConvention namingConvention;

    /**
     * JpaInstrumentationを設定します。
     * @param jpaInstrumentation 設定するJpaInstrumentation
     */
    public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
        this.jpaInstrumentation = jpaInstrumentation;
    }

    /**
     * NamingConventionを設定します。
     * @param namingConvention 設定するNamingConvention
     */
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * 引数のClassTransformerをJpaInstrumentationに設定して、Entityクラスのエンハンス処理を行います。
     * @see oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo#addTransformer(javax.persistence.spi.ClassTransformer)
     */
    @Override
    public void addTransformer(ClassTransformer transformer) {
        if (jpaInstrumentation != null) {
            jpaInstrumentation.addTransformer(transformer, getClassLoader());
        }
    }

    /**
     * classLoaderが設定されてない場合、Thread.currentThread().getContextClassLoader()で取得したClassLoaderを設定して返します。
     * @see oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo#getClassLoader()
     */
    @Override
    public ClassLoader getClassLoader() {
        if (super.getClassLoader() == null) {
            setClassLoader(Thread.currentThread().getContextClassLoader());
        }
        return super.getClassLoader();
    }

    /**
     * newTempClassLoaderが設定されてない場合、S2TopLinkTempClassLoaderを生成し、設定して返します。
     * @see oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo#getNewTempClassLoader()
     */
    @Override
    public ClassLoader getNewTempClassLoader() {
        if (super.getNewTempClassLoader() == null) {
            if (!excludeUnlistedClasses() && tempClassNameSet == null) {
                tempClassNameSet = PersistenceUnitProcessor.buildClassSet(this,
                        getClassLoader());
            }
            S2TopLinkTempClassLoader loader = new S2TopLinkTempClassLoader(getClassLoader(), namingConvention);
            loader.setTempClassNameSet(tempClassNameSet);
            setNewTempClassLoader(loader);
        }
        return super.getNewTempClassLoader();
    }

    /**
     * 設定されたjtaDataSourceがDataSourceImplオブジェクトだった場合、dataSource名を取得して新規にDataSourceを作成します。
     * @see oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo#setJtaDataSource(javax.sql.DataSource)
     */
    @Override
    @Binding(bindingType = BindingType.NONE)
    public void setJtaDataSource(DataSource jtaDataSource) {
        if (jtaDataSource != null && jtaDataSource instanceof DataSourceImpl) {
            String jtaDsName = DataSourceImpl.class.cast(jtaDataSource)
                    .getName();
            jtaDataSource = new SingletonDataSourceProxy(jtaDsName);
        }
        super.setJtaDataSource(jtaDataSource);
    }

    /**
     * 設定されたnonJtaDataSourceがDataSourceImplオブジェクトだった場合、dataSource名を取得して新規にDataSourceを作成します。
     * @see oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo#setNonJtaDataSource(javax.sql.DataSource)
     */
    @Override
    @Binding(bindingType = BindingType.NONE)
    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        if (nonJtaDataSource != null
                && nonJtaDataSource instanceof DataSourceImpl) {
            String nonJtaDsName = DataSourceImpl.class.cast(nonJtaDataSource)
                    .getName();
            nonJtaDataSource = new SingletonDataSourceProxy(nonJtaDsName);
        }
        super.setNonJtaDataSource(nonJtaDataSource);
    }

}
