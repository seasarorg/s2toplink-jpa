package example.entity;

import java.util.List;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;

public class InsertTest extends S2TestCase {
    
    private EntityManager em;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(InsertTest.class.getSimpleName() + ".dicon");
    }

	public void testPersistTx() {
		
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
