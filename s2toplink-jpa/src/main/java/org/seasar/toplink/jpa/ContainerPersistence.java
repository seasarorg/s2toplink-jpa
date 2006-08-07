package org.seasar.toplink.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

public interface ContainerPersistence {

	PersistenceUnitInfo getPersistenceUnitInfo(String persistenceUnitName);
	
	EntityManagerFactory getContainerEntityManagerFactory(String persistenceUnitName, Map map);
	
}
