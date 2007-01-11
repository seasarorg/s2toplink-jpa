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
 * @author Hidenoshin Yoshida
 *
 */
public interface S2TopLinkPersistenceUnitInfo extends PersistenceUnitInfo {

    public void setPersistenceUnitName(String persistenceUnitName);

    public void setPersistenceProviderClassName(String persistenceProviderClassName);
    
    public void setTransactionType(PersistenceUnitTransactionType persistenceUnitTransactionType);
    
    public void setJtaDataSource(DataSource jtaDataSource);
    
    public void setNonJtaDataSource(DataSource nonJtaDataSource);

    public void setMappingFileNames(List<String> mappingFiles);

    public void setJarFileUrls(List<URL> jarFileUrls);
    
    public void setPersistenceUnitRootUrl(URL persistenceUnitRootUrl);
    
    public void setManagedClassNames(List<String> managedClassNames);
    
    public void setExcludeUnlistedClasses(boolean excludeUnlistedClasses);

    public void setProperties(Properties properties);

    public void setNewTempClassLoader(ClassLoader loader);
    
    public void setClassLoader(ClassLoader loader);

}
