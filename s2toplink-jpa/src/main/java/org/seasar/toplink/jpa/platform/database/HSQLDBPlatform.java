/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.toplink.jpa.platform.database;

import oracle.toplink.essentials.platform.database.HSQLPlatform;
import oracle.toplink.essentials.queryframework.ValueReadQuery;

/**
 * @author Hidenoshin Yoshida
 *
 */
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
