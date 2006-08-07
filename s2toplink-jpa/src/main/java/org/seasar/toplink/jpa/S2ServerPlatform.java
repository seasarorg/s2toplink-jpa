package org.seasar.toplink.jpa;

import oracle.toplink.essentials.internal.sessions.DatabaseSessionImpl;
import oracle.toplink.essentials.platform.server.ServerPlatformBase;

public class S2ServerPlatform extends ServerPlatformBase {

	public S2ServerPlatform(DatabaseSessionImpl newDatabaseSession) {
		super(newDatabaseSession);
	}

	@Override
	public Class getExternalTransactionControllerClass() {
    	if (externalTransactionControllerClass == null){
    		externalTransactionControllerClass = S2TransactionController.class;
    	}
        return externalTransactionControllerClass;
	}

}
