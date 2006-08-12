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

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author da-yoshi
 *
 */
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
