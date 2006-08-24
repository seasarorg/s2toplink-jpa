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
package org.seasar.toplink.jpa;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

	private String persistenceUnitName;
	
	private String description;
	
	private String persistenceProviderClassName;
	
	private PersistenceUnitTransactionType transactionType;

	private DataSource jtaDataSource;
	
	private DataSource nonJtaDataSource;
	
	private boolean excludeUnlistedClasses;
	
	private URL persistenceUnitRootUrl;
	
	private Properties properties = new Properties();
	
	private List<String> mappingFileNames = new ArrayList<String>();
	
	private List<URL> jarFileUrls = new ArrayList<URL>();
	
	private List<String> managedClassNames = new ArrayList<String>();
	
	private JpaInstrumentation jpaInstrumentation;
	
	public PersistenceUnitInfoImpl() {
	}
	
	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public void setPersistenceUnitName(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPersistenceProviderClassName() {
		return persistenceProviderClassName;
	}

	public void setPersistenceProviderClassName(String persistenceProviderClassName) {
		this.persistenceProviderClassName = persistenceProviderClassName;
	}

	public PersistenceUnitTransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(PersistenceUnitTransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public DataSource getJtaDataSource() {
		return jtaDataSource;
	}

	public void setJtaDataSource(DataSource jtaDataSource) {
		this.jtaDataSource = jtaDataSource;
	}

	public DataSource getNonJtaDataSource() {
		return nonJtaDataSource;
	}

	public void setNonJtaDataSource(DataSource nonJtaDataSource) {
		this.nonJtaDataSource = nonJtaDataSource;
	}

	public List<String> getMappingFileNames() {
		return mappingFileNames;
	}
	
	public List<URL> getJarFileUrls() {
		return jarFileUrls;
	}

	public URL getPersistenceUnitRootUrl() {
		return persistenceUnitRootUrl;
	}

	public void setPersistenceUnitRootUrl(URL persistenceUnitRootUrl) {
		this.persistenceUnitRootUrl = persistenceUnitRootUrl;
	}

	public List<String> getManagedClassNames() {
		return managedClassNames;
	}
	
	public boolean excludeUnlistedClasses() {
		return excludeUnlistedClasses;
	}

	public void setExcludeUnlistedClasses(boolean excludeUnlistedClasses) {
		this.excludeUnlistedClasses = excludeUnlistedClasses;
	}

	public Properties getProperties() {
		return properties;
	}

	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public void addTransformer(final ClassTransformer transformer) {
		if (getJpaInstrumentation() != null) {
			getJpaInstrumentation().transform(getClassLoader(), transformer);
		}
	}

	public ClassLoader getNewTempClassLoader() {
		ClassLoader cl = getClassLoader();
		List<URL> urlList = new ArrayList<URL>();
		if ("file".equals(getPersistenceUnitRootUrl().getProtocol())) {
			String root = getPersistenceUnitRootUrl().toString() + "/";
			try {
				urlList.add(new URL(root));
			} catch (MalformedURLException e) {
                throw new PersistenceException(e);
			}
		} else {
			urlList.add(getPersistenceUnitRootUrl());
		}
		urlList.addAll(getJarFileUrls());
		return new TempClassLoader(urlList.toArray(new URL[urlList.size()]), cl);
	}
	
	public class TempClassLoader extends URLClassLoader {
		
		public TempClassLoader(URL[] urls) {
			super(urls);
		}

		public TempClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public TempClassLoader(URL[] urls, ClassLoader parent,
				URLStreamHandlerFactory factory) {
			super(urls, parent, factory);
		}

		@Override
		protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			if (!name.startsWith("java.")
			    && !name.startsWith("javax.")) {
				Class<?> c = findLoadedClass(name);
				if (c == null) {
					try {
						c = findClass(name);
					} catch (ClassNotFoundException e) {
						return super.loadClass(name, resolve);
					}
				}
				if (resolve) {
					resolveClass(c);
				}
				return c;
			} else {
				return super.loadClass(name, resolve);
			}
		}

	}

	public JpaInstrumentation getJpaInstrumentation() {
		return jpaInstrumentation;
	}

	public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
		this.jpaInstrumentation = jpaInstrumentation;
	}

	
}
