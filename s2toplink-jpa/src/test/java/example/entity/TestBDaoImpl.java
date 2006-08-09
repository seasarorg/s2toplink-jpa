package example.entity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class TestBDaoImpl implements TestBDao {
    
    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager em;

    /**
     * @param em 設定する em。
     */
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void insertTestB(TestB testb) {
        em.persist(testb);
    }

    public TestB getTestB(Integer id) {
        return em.find(TestB.class, id);
    }

}
