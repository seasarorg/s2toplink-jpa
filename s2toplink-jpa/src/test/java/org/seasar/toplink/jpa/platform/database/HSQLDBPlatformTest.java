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
package org.seasar.toplink.jpa.platform.database;

import oracle.toplink.essentials.queryframework.ValueReadQuery;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 *
 */
@RunWith(Seasar2.class)
public class HSQLDBPlatformTest {
    
    private HSQLDBPlatform platform = new HSQLDBPlatform();

    /**
     * {@link org.seasar.toplink.jpa.platform.database.HSQLDBPlatform#supportsForeignKeyConstraints()} のためのテスト・メソッド。
     */
    public void testSupportsForeignKeyConstraints() {
        assertTrue(platform.supportsForeignKeyConstraints());
    }

    /**
     * {@link org.seasar.toplink.jpa.platform.database.HSQLDBPlatform#supportsNativeSequenceNumbers()} のためのテスト・メソッド。
     */
    public void testSupportsNativeSequenceNumbers() {
        assertTrue(platform.supportsNativeSequenceNumbers());
    }

    /**
     * {@link org.seasar.toplink.jpa.platform.database.HSQLDBPlatform#buildSelectQueryForNativeSequence(java.lang.String, java.lang.Integer)} のためのテスト・メソッド。
     */
    public void testBuildSelectQueryForNativeSequenceStringInteger() {
        ValueReadQuery query = platform.buildSelectQueryForNativeSequence("SEQUENCE", 1);
        assertEquals("SELECT NEXT VALUE FOR SEQUENCE FROM DUAL_SEQUENCE", query.getCall().getQueryString());
    }

    /**
     * {@link org.seasar.toplink.jpa.platform.database.HSQLDBPlatform#getQualifiedSequenceName(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetQualifiedSequenceName() {
        assertEquals("SEQUENCE", platform.getQualifiedSequenceName("SEQUENCE"));
    }

}
