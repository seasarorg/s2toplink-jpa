package org.seasar.toplink.jpa.impl;

import org.seasar.toplink.jpa.JpaInstrumentation;
import org.seasar.toplink.jpa.PersistenceUnitInfoCreator;
import org.seasar.toplink.jpa.PersistenceUnitInfoImpl;

public class PersistenceUnitInfoCreatorImpl implements
		PersistenceUnitInfoCreator {

	private JpaInstrumentation jpaInstrumentation;
	
	public void setJpaInstrumentation(JpaInstrumentation jpaInstrumentation) {
		this.jpaInstrumentation = jpaInstrumentation;
	}

	public PersistenceUnitInfoImpl createPersistenceUnitInfo() {
		
		PersistenceUnitInfoImpl impl = new PersistenceUnitInfoImpl();
		impl.setJpaInstrumentation(jpaInstrumentation);
		return impl;
	}

}
