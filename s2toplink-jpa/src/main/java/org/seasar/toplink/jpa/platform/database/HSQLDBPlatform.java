package org.seasar.toplink.jpa.platform.database;

import oracle.toplink.essentials.platform.database.HSQLPlatform;

public class HSQLDBPlatform extends HSQLPlatform {

    /**
     * 
     */
    private static final long serialVersionUID = -8108622378935028872L;

    public boolean supportsForeignKeyConstraints() {
        return true;
    }
    
    public boolean supportsNativeSequenceNumbers() {
        return true;
    }
}
