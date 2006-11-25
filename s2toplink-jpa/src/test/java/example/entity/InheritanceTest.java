/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package example.entity;

import java.util.List;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class InheritanceTest extends S2TestCase {
    
    private EntityManager em;

    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(InheritanceTest.class.getSimpleName() + ".dicon");
    }
    
    @SuppressWarnings("unchecked")
    public void testInheritanceTx() {
        TestE e = new TestE();
        e.setName("TESTE");
        em.persist(e);
        
        TestF f = new TestF();
        f.setName("TESTF");
        f.setFname("F1");
        em.persist(f);
        
        TestG g = new TestG();
        g.setName("TESTG");
        g.setGname("G1");
        em.persist(g);
        
        em.flush();
        em.clear();
        
        List<TestE> list = em.createQuery("SELECT e FROM TestE e").getResultList();
        for (TestE teste : list) {
            System.out.println("ID:" + teste.getId());
            System.out.println("NAME:" + teste.getName());
            System.out.println("VERSION:" + teste.getVersion());
            if (teste instanceof TestF) {
                System.out.println("FNAME:" + TestF.class.cast(teste).getFname());
            }
            if (teste instanceof TestG) {
                System.out.println("GNAME:" + TestG.class.cast(teste).getGname());
            }
        }
        
        EntityDesc desc = EntityDescFactory.getEntityDesc(TestG.class);
        System.out.println(desc);
        
    }

}
