/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import javax.persistence.spi.ClassTransformer;
import javax.sql.DataSource;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.toplink.jpa.JpaInstrumentation;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;
import org.seasar.toplink.jpa.datasource.DataSourceProxy;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkPersistenceUnitInfoImpl extends SEPersistenceUnitInfo
        implements S2TopLinkPersistenceUnitInfo {

//    protected Set<String> tempClassNameSet;

    protected JpaInstrumentation jpaInstrumentation;
    
//    protected S2JavaSECMPInitializer javaSECMPInitializer;

    public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
        this.jpaInstrumentation = jpaInstrumentation;
    }

//    public void setJavaSECMPInitializer(S2JavaSECMPInitializer javaSECMPInitializer) {
//        this.javaSECMPInitializer = javaSECMPInitializer;
//    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        if (jpaInstrumentation != null) {
            jpaInstrumentation.addTransformer(transformer, getClassLoader());
        }
    }

//    @Override
//    public ClassLoader getClassLoader() {
//        return Thread.currentThread().getContextClassLoader();
//    }

//    @Override
//    public ClassLoader getNewTempClassLoader() {
//
//        if (tempClassNameSet == null) {
//            tempClassNameSet = PersistenceUnitProcessor.buildClassSet(this, getClassLoader());
//        }
//        URL[] urls = null;
//        if (getClassLoader() instanceof URLClassLoader) {
//            urls = URLClassLoader.class.cast(getClassLoader()).getURLs();
//        } else {
//            List<URL> urlList = new ArrayList<URL>();
//            if (getPersistenceUnitRootUrl() != null) {
//                urlList.add(getPersistenceUnitRootUrl());
//            }
//            if (getJarFileUrls() != null) {
//                urlList.addAll(getJarFileUrls());
//            }
//            urls = urlList.toArray(new URL[urlList.size()]);
//        }
//        return javaSECMPInitializer.createTempLoader(tempClassNameSet);
//    }

//    @Override
//    public void setClassLoader(ClassLoader loader) {
//    }
//
//    @Override
//    public void setNewTempClassLoader(ClassLoader loader) {
//    }

    @Override
    @Binding(bindingType = BindingType.NONE)
    public void setJtaDataSource(DataSource jtaDataSource) {
        if (jtaDataSource != null && jtaDataSource instanceof DataSourceImpl) {
            String jtaDsName = DataSourceImpl.class.cast(jtaDataSource)
                    .getName();
            jtaDataSource = new DataSourceProxy(jtaDsName);
        }
        super.setJtaDataSource(jtaDataSource);
    }

    @Override
    @Binding(bindingType = BindingType.NONE)
    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        if (nonJtaDataSource != null
                && nonJtaDataSource instanceof DataSourceImpl) {
            String nonJtaDsName = DataSourceImpl.class.cast(nonJtaDataSource)
                    .getName();
            nonJtaDataSource = new DataSourceProxy(nonJtaDsName);
        }
        super.setNonJtaDataSource(nonJtaDataSource);
    }

}
