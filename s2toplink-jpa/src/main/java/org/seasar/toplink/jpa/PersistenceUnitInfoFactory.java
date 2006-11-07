package org.seasar.toplink.jpa;

import javax.persistence.spi.PersistenceUnitInfo;


public interface PersistenceUnitInfoFactory {

    PersistenceUnitInfo getPersistenceUnitInfo(String unitName);
    
    void addAutoDetectResult(PersistenceUnitInfo persistenceUnitInfo);
}
