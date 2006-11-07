package org.seasar.toplink.jpa.platform.server;

import oracle.toplink.essentials.internal.sessions.DatabaseSessionImpl;
import oracle.toplink.essentials.platform.server.ServerPlatformBase;

import org.seasar.toplink.jpa.transaction.S2TransactionProxyController;

public class S2WebServerPlatform extends ServerPlatformBase {

    public S2WebServerPlatform(DatabaseSessionImpl newDatabaseSession) {
        super(newDatabaseSession);
    }

    @Override
    public Class getExternalTransactionControllerClass() {
        if (externalTransactionControllerClass == null){
            externalTransactionControllerClass = S2TransactionProxyController.class;
        }
        return externalTransactionControllerClass;
    }

}
