package org.seasar.toplink.jpa.impl;

import java.util.Set;

import javax.persistence.spi.ClassTransformer;
import javax.sql.DataSource;

import oracle.toplink.essentials.ejb.cmp3.persistence.PersistenceUnitProcessor;
import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

import org.seasar.toplink.jpa.JpaInstrumentation;
import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;
import org.seasar.toplink.jpa.datasource.DataSourceProxy;

public class S2TopLinkPersistenceUnitInfoImpl extends SEPersistenceUnitInfo
        implements S2TopLinkPersistenceUnitInfo {
    
    protected Set<String> tempClassNameSet;
    
    protected JpaInstrumentation jpaInstrumentation;
    
    protected S2JavaSECMPInitializer javaSECMPInitializer;

    public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
        this.jpaInstrumentation = jpaInstrumentation;
    }

    public void setJavaSECMPInitializer(
            S2JavaSECMPInitializer javaSECMPInitializer) {
        this.javaSECMPInitializer = javaSECMPInitializer;
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        if (jpaInstrumentation != null) {
            jpaInstrumentation.addTransformer(transformer, getClassLoader());
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        
        if (tempClassNameSet == null) {
            tempClassNameSet =  PersistenceUnitProcessor.buildClassSet(this);
        }

        return javaSECMPInitializer.createTempLoader(tempClassNameSet);
    }

    @Override
    public void setClassLoader(ClassLoader loader) {
    }

    @Override
    public void setNewTempClassLoader(ClassLoader loader) {
    }

    @Override
    public void setJtaDataSource(DataSource jtaDataSource) {
        if (jtaDataSource != null && jtaDataSource instanceof DataSourceImpl) {
            String jtaDsName = DataSourceImpl.class.cast(jtaDataSource)
                    .getName();
            jtaDataSource = new DataSourceProxy(jtaDsName);
        }
        super.setJtaDataSource(jtaDataSource);
    }

    @Override
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
