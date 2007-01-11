/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import oracle.toplink.essentials.internal.sessions.DatabaseSessionImpl;
import oracle.toplink.essentials.platform.server.ServerPlatformBase;

import org.seasar.toplink.jpa.transaction.S2TransactionControllerForTest;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2ServerPlatformForTest extends ServerPlatformBase {

    /**
     * @param newDatabaseSession
     */
    public S2ServerPlatformForTest(DatabaseSessionImpl newDatabaseSession) {
        super(newDatabaseSession);
    }

    /* (non-Javadoc)
     * @see oracle.toplink.essentials.platform.server.ServerPlatformBase#getExternalTransactionControllerClass()
     */
    @Override
    public Class getExternalTransactionControllerClass() {
        if (externalTransactionControllerClass == null){
            externalTransactionControllerClass = S2TransactionControllerForTest.class;
        }
        return externalTransactionControllerClass;
    }

}
