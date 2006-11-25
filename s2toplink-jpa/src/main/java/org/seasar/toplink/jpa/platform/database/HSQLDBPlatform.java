package org.seasar.toplink.jpa.platform.database;

import oracle.toplink.essentials.platform.database.HSQLPlatform;
import oracle.toplink.essentials.queryframework.ValueReadQuery;

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
    
    public ValueReadQuery buildSelectQueryForNativeSequence(String seqName, Integer size) {
        String sequenceName = getQualifiedSequenceName(seqName);
        return new ValueReadQuery("SELECT NEXT VALUE FOR "  + sequenceName + " FROM DUAL_" + sequenceName);
    }

    protected String getQualifiedSequenceName(String seqName) {
        if (getTableQualifier().equals("")) {
            return seqName;
        } else {
            return getTableQualifier() + "." + seqName;
        }
    }
}
