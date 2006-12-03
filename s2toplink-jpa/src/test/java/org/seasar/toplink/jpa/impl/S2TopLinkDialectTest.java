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
package org.seasar.toplink.jpa.impl;

import java.sql.Connection;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.Dialect;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkDialectTest extends S2TestCase {
    
    private EntityManager entityManager;
    
    private EntityManager mockEntityManager;

    private Dialect dialect;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(S2TopLinkDialectTest.class.getSimpleName() + ".dicon");
    }

    public void testGetConnectionTx() {
        
        Connection con = dialect.getConnection(entityManager);
        assertNotNull(con);
        con = dialect.getConnection(mockEntityManager);
        assertNull(con);
        
    }
    
}
