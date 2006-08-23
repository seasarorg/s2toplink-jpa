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
package example.dao.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import example.dao.TestBDao;
import example.entity.TestB;


/**
 * @author Hidenoshin Yoshida
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
