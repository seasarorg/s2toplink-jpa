package example.entity;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class TestBDaoImplTest {
    
    private TestBDao testBDao;
    
    private EntityManager em;
    
    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.insertTestB(TestB)' のためのテスト・メソッド
     */
    public void testInsertTestB() {
        
        TestB b = new TestB();
        b.setName("テスト");
        testBDao.insertTestB(b);
        
        TestB expected = em.find(TestB.class, b.getId());

        assertEquals(expected, b);
    }

    /*
     * 'org.seasar.toplink.jpa.impl.TestBDaoImpl.getTestB(Integer)' のためのテスト・メソッド
     */
    public void testGetTestB() {
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
