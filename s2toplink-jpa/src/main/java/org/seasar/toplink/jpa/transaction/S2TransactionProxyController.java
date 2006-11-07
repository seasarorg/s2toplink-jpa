package org.seasar.toplink.jpa.transaction;

import javax.transaction.TransactionManager;

import oracle.toplink.essentials.transaction.JTATransactionController;

public class S2TransactionProxyController extends JTATransactionController {
    
    @Override
    protected TransactionManager acquireTransactionManager() throws Exception {
        return  new TransactionManagerProxy();
    }

}
