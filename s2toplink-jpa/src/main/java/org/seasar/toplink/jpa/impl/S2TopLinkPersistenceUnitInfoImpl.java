package org.seasar.toplink.jpa.impl;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.persistence.spi.ClassTransformer;
import javax.sql.DataSource;

import org.seasar.toplink.jpa.S2TopLinkPersistenceUnitInfo;
import org.seasar.toplink.jpa.datasource.DelegateDataSource;

import oracle.toplink.essentials.ejb.cmp3.persistence.SEPersistenceUnitInfo;
import oracle.toplink.essentials.internal.ejb.cmp3.jdbc.base.DataSourceImpl;

public class S2TopLinkPersistenceUnitInfoImpl extends SEPersistenceUnitInfo implements S2TopLinkPersistenceUnitInfo {

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // TODO 自動生成されたメソッド・スタブ
        super.addTransformer(transformer);
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        List<URL> urls = new ArrayList<URL>();
        if (getPersistenceUnitRootUrl() != null) {
            urls.add(getPersistenceUnitRootUrl());
        }
        if (getJarFileUrls() != null) {
            urls.addAll(getJarFileUrls());
        }
        return new TempClassLoader(urls.toArray(new URL[urls.size()]), getClassLoader());
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
            String jtaDsName = DataSourceImpl.class.cast(jtaDataSource).getName();
            jtaDataSource = new DelegateDataSource(jtaDsName);
        }
        super.setJtaDataSource(jtaDataSource);
    }

    @Override
    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        if (nonJtaDataSource != null && nonJtaDataSource instanceof DataSourceImpl) {
            String nonJtaDsName = DataSourceImpl.class.cast(nonJtaDataSource).getName();
            nonJtaDataSource = new DelegateDataSource(nonJtaDsName);
        }
        super.setNonJtaDataSource(nonJtaDataSource);
    }

    public class TempClassLoader extends URLClassLoader {

        public TempClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
            super(urls, parent, factory);
        }

        public TempClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public TempClassLoader(URL[] urls) {
            super(urls);
        }

        @Override
        public Enumeration<URL> getResources(String name) throws IOException {
            return getParent().getResources(name);
        }
        
    }
}
