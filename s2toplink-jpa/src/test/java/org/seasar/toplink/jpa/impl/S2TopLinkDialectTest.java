package org.seasar.toplink.jpa.impl;

import java.sql.Connection;

import javax.persistence.EntityManager;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.Dialect;

public class S2TopLinkDialectTest extends S2TestCase {
    
    private EntityManager em;

    private Dialect dialect;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(S2TopLinkDialectTest.class.getSimpleName() + ".dicon");
    }

    public void testGetConnectionTx() {
        
        Connection con = dialect.getConnection(em);
        System.out.println(con);
        
    }

}
