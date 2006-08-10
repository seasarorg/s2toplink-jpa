package example.entity;

import org.seasar.framework.ejb.unit.S2EJB3TestCase;
import org.seasar.framework.unit.annotation.Rollback;


public class TestBDaoImplTest extends S2EJB3TestCase {
    
    private TestBDao testBDao;
    
    /* (非 Javadoc)
     * @see org.seasar.framework.ejb.unit.S2EJB3TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        include("TestBDaoImplTest.dicon");
    }

    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.insertTestB(TestB)' のためのテスト・メソッド
     */
    @Rollback
    public void testInsertTestB() {
        
        TestB b = new TestB();
        b.setName("テスト");
        testBDao.insertTestB(b);
        
        TestB expected = getEntityManager().find(TestB.class, b.getId());

        assertEquals(expected, b);
    }

    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.getTestB(Integer)' のためのテスト・メソッド
     */
    @Rollback
    public void testGetTestB() {
        TestB b = new TestB();
        b.setName("テスト");
        testBDao.insertTestB(b);
        
        getEntityManager().flush();
        getEntityManager().clear();
        
        TestB b2 = testBDao.getTestB(b.getId());
        
        assertEquals(b.getName(), b2.getName());
        assertEquals(b.getVersion(), b2.getVersion());

    }

}
