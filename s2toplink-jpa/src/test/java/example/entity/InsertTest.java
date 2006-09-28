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

/**
 * @author Hidenoshin Yoshida
 *
 */
public class InsertTest extends S2TestCase {
    
    private EntityManager em;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(InsertTest.class.getSimpleName() + ".dicon");
    }

	@SuppressWarnings("unchecked")
    public void testPersistTx() {
		
		TestA a1 = new TestA();
		a1.setName("テスト");
		em.persist(a1);
		
		List<TestA> list = (List<TestA>) em.createQuery("SELECT a FROM TestA a").getResultList();
        assertEquals(1, list.size());
        TestA a = list.get(0);
        assertEquals("テスト", a.getName());
        assertEquals(1, a.getVersion().intValue());
	}

}
