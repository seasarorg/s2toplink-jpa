/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.toplink.jpa.transaction;

import javax.transaction.TransactionManager;

import org.junit.runner.RunWith;
import org.seasar.extension.jta.SingletonTransactionManagerProxy;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author hid-yoshida
 *
 */
@RunWith(Seasar2.class)
public class SingletonS2TransactionControllerTest {
    
    /**
     * {@link org.seasar.toplink.jpa.transaction.SingletonS2TransactionController#acquireTransactionManager()} のためのテスト・メソッド。
     * @throws Exception 
     */
    public void testAcquireTransactionManager() throws Exception {
        SingletonS2TransactionController controller = new SingletonS2TransactionController();

        TransactionManager tm = controller.acquireTransactionManager();
        assertTrue(tm instanceof SingletonTransactionManagerProxy);
    }

}
