/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
// Copyright (c) 1998, 2006, Oracle. All rights reserved.  
package oracle.toplink.essentials.platform.database;

import java.util.*;
import java.io.*;
import java.sql.*;
import oracle.toplink.essentials.exceptions.*;
import oracle.toplink.essentials.queryframework.*;
import oracle.toplink.essentials.internal.helper.*;
import oracle.toplink.essentials.expressions.*;
import oracle.toplink.essentials.internal.expressions.*;
import oracle.toplink.essentials.internal.databaseaccess.*;
import oracle.toplink.essentials.internal.sessions.AbstractSession;

/**
 * <p><b>Purpose</b>: Provides H2 specific behaviour.
 */
public class H2Platform extends DatabasePlatform {
    public H2Platform() {
    }

    protected Hashtable buildFieldTypes() {
        Hashtable fieldTypeMapping;

        fieldTypeMapping = super.buildFieldTypes();
        fieldTypeMapping.put(Boolean.class, new FieldTypeDefinition("TINYINT", false));
        fieldTypeMapping.put(Integer.class, new FieldTypeDefinition("INTEGER", false));
        fieldTypeMapping.put(Long.class, new FieldTypeDefinition("NUMERIC", 19));
        fieldTypeMapping.put(Float.class, new FieldTypeDefinition("REAL", false));
        fieldTypeMapping.put(Double.class, new FieldTypeDefinition("REAL", false));
        fieldTypeMapping.put(Short.class, new FieldTypeDefinition("SMALLINT", false));
        fieldTypeMapping.put(Byte.class, new FieldTypeDefinition("SMALLINT", false));
        fieldTypeMapping.put(java.math.BigInteger.class, new FieldTypeDefinition("NUMERIC", 38));
        fieldTypeMapping.put(java.math.BigDecimal.class, new FieldTypeDefinition("NUMERIC", 38).setLimits(38, -19, 19));
        fieldTypeMapping.put(Number.class, new FieldTypeDefinition("NUMERIC", 38).setLimits(38, -19, 19));
        fieldTypeMapping.put(Byte[].class, new FieldTypeDefinition("BINARY", false));
        fieldTypeMapping.put(Character[].class, new FieldTypeDefinition("LONGVARCHAR", false));
        fieldTypeMapping.put(byte[].class, new FieldTypeDefinition("BINARY", false));
        fieldTypeMapping.put(char[].class, new FieldTypeDefinition("LONGVARCHAR", false));
        fieldTypeMapping.put(java.sql.Blob.class, new FieldTypeDefinition("BINARY", false));
        fieldTypeMapping.put(java.sql.Clob.class, new FieldTypeDefinition("LONGVARCHAR", false));        
        fieldTypeMapping.put(java.sql.Date.class, new FieldTypeDefinition("DATE", false));
        fieldTypeMapping.put(java.sql.Time.class, new FieldTypeDefinition("TIME", false));
        fieldTypeMapping.put(java.sql.Timestamp.class, new FieldTypeDefinition("TIMESTAMP", false));

        return fieldTypeMapping;
    }
    
    
    // public boolean isHSQL() {
    //     return true;
    // }    

    public boolean isH2() {
        return true;
    }

    public boolean supportsForeignKeyConstraints() {
        return true;
    }
    
    public ValueReadQuery buildSelectQueryForNativeSequence(String seqName, Integer size) {
        return new ValueReadQuery("CALL NEXT VALUE FOR " + getQualifiedSequenceName(seqName));
        // return new ValueReadQuery("SELECT " + getQualifiedSequenceName(seqName) + ".NEXTVAL FROM DUAL");
    }    
    
    public boolean supportsNativeSequenceNumbers() {
        return true;
    }
    
    protected String getQualifiedSequenceName(String seqName) {
        if (getTableQualifier().equals("")) {
            return seqName;
        } else {
            return getTableQualifier() + "." + seqName;
        }
    }
    
    public boolean supportsSelectForUpdateNoWait() {
        return true;
    }
    
    protected ExpressionOperator todayOperator() {
        return ExpressionOperator.simpleFunctionNoParentheses(ExpressionOperator.Today, "SYSDATE");
    }        
    
    protected void initializePlatformOperators() {
        super.initializePlatformOperators();
        addOperator(ExpressionOperator.simpleMath(ExpressionOperator.Concat, "||"));
    }    
    
    public boolean shouldUseJDBCOuterJoinSyntax() {
        return false;
    }
        
}
