package org.seasar.toplink.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.seasar.framework.ejb.unit.S2EJB3TestCase;
import org.seasar.framework.unit.annotation.Rollback;

public class InsertTest extends S2EJB3TestCase {
	
	@Rollback
	public void testPersist() {
		
		EntityManager em = getEntityManager();
		TestA a1 = new TestA();
		a1.setName("テスト");
		em.persist(a1);
		
		List<TestA> list = (List<TestA>) em.createQuery("SELECT a FROM TestA a").getResultList();
        assertEquals(1, list.size());
        TestA a = list.get(0);
        assertEquals(1, a.getId().intValue());
        assertEquals("テスト", a.getName());
        assertEquals(1, a.getVersion().intValue());
	}

}
