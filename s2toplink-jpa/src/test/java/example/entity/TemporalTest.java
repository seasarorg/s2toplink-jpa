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
package example.entity;

import java.sql.Types;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TemporalTest extends S2TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }



    public void testTemporalTx() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(TestH.class);
        assertEquals(Types.DATE, desc.getAttributeDesc("date").getSqlType());
        assertEquals(Types.TIME, desc.getAttributeDesc("time").getSqlType());
        assertEquals(Types.TIMESTAMP, desc.getAttributeDesc("timestamp").getSqlType());
        assertEquals(Types.TIMESTAMP, desc.getAttributeDesc("calendar").getSqlType());
    }

}
