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
package org.seasar.toplink.jpa.unit;

import java.util.Calendar;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;

import example.entity.TestC;
import example.entity.TestCId;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityReaderProviderTest extends S2TestCase {
    
    private TopLinkEntityReaderProvider provider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(TopLinkEntityReaderProviderTest.class.getSimpleName() + ".dicon");
    }

    public void testCreateEntityReaderObjectTx() {
        
        TestC c = new TestC();
        TestCId id = new TestCId();
        id.setId1(1);
        id.setId2(2);
        c.setId(id);
        c.setInsertDate(Calendar.getInstance());
        c.setName("test");
        TopLinkEntityReader reader = provider.createEntityReader(c);
        DataSet dataSet = reader.read();
        System.out.println(dataSet);
    }

}
