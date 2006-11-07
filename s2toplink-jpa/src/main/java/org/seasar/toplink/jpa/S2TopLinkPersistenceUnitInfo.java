package org.seasar.toplink.jpa;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

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
