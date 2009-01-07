/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.toplink.jpa.platform.server;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.toplink.jpa.transaction.SingletonS2TransactionController;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 *
 */
@RunWith(Seasar2.class)
public class S2ServerPlatformTest {

    private S2ServerPlatform serverPlatform = new S2ServerPlatform(null);

    /**
     * {@link org.seasar.toplink.jpa.platform.server.S2ServerPlatform#getExternalTransactionControllerClass()} のためのテスト・メソッド。
     */
    public void testGetExternalTransactionControllerClass() {
        assertEquals(SingletonS2TransactionController.class, serverPlatform.getExternalTransactionControllerClass());
    }

}
