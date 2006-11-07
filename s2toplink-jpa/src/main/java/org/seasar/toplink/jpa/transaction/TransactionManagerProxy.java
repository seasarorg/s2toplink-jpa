package org.seasar.toplink.jpa.transaction;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

public class TransactionManagerProxy implements TransactionManager {
    
    protected TransactionManager getTransactionManager() {
        S2Container container = SingletonS2ContainerFactory.getContainer();
        return (TransactionManager) container.getComponent(TransactionManager.class);
    }

    public void begin() throws NotSupportedException, SystemException {
        getTransactionManager().begin();

    }

    public void commit() throws HeuristicMixedException,
            HeuristicRollbackException, IllegalStateException,
            RollbackException, SecurityException, SystemException {
        getTransactionManager().commit();
    }

    public int getStatus() throws SystemException {
        return getTransactionManager().getStatus();
    }

    public Transaction getTransaction() throws SystemException {
        return getTransactionManager().getTransaction();
    }

    public void resume(final Transaction tx) throws IllegalStateException,
            InvalidTransactionException, SystemException {
        getTransactionManager().resume(tx);

    }

    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        getTransactionManager().rollback();

    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getTransactionManager().setRollbackOnly();
    }

    public void setTransactionTimeout(final int timeout) throws SystemException {
        getTransactionManager().setTransactionTimeout(timeout);

    }

    public Transaction suspend() throws SystemException {
        return getTransactionManager().suspend();
    }

}
