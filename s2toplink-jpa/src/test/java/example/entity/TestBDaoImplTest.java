package example.entity;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;

public class TestBDaoImplTest extends S2TestCase {
    
    private TestBDao testBDao;
    
    private EntityManager em;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(TestBDaoImplTest.class.getSimpleName() + ".dicon");
    }

    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.insertTestB(TestB)' のためのテスト・メソッド
     */
    public void testInsertTestBTx() {
        
        TestB b = new TestB();
        b.setName("テスト");
        testBDao.insertTestB(b);
        
        TestB expected = em.find(TestB.class, b.getId());

        assertEquals(expected, b);
    }

    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.getTestB(Integer)' のためのテスト・メソッド
     */
    public void testGetTestBTx() {
        TestB b = new TestB();
        b.setName("テスト");
        testBDao.insertTestB(b);
        
        em.flush();
        em.clear();
        
        TestB b2 = testBDao.getTestB(b.getId());
        
        assertEquals(b.getName(), b2.getName());
        assertEquals(b.getVersion(), b2.getVersion());

    }

}
