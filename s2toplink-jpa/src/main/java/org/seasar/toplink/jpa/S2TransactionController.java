package org.seasar.toplink.jpa;

import javax.transaction.TransactionManager;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import oracle.toplink.essentials.transaction.JTATransactionController;

public class S2TransactionController extends JTATransactionController {

	protected TransactionManager acquireTransactionManager() throws Exception {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (TransactionManager) container.getComponent(TransactionManager.class);
    }
}
