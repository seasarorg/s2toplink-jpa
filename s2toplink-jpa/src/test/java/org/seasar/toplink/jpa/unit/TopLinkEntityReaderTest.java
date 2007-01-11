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
package org.seasar.toplink.jpa.unit;

import javax.persistence.EntityManager;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.impl.SqlReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderFactory;
import org.seasar.framework.unit.S2Assert;
import org.seasar.toplink.jpa.entity.Customer;
import org.seasar.toplink.jpa.entity.Enemy;

/**
 * @author hid-yoshida
 *
 */
public class TopLinkEntityReaderTest extends S2TestCase {
    
    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create("org/seasar/toplink/jpa/unit/TopLinkEntityReaderTest.dicon");
        container.init();
        try {
            SqlReader reader = (SqlReader)
                container.getComponent(SqlReader.class);
            XlsWriter writer = (XlsWriter)
                container.getComponent(XlsWriter.class);
            writer.write(reader.read());
        } finally {
            container.destroy();
        }
 
    }
    
    private EntityManager em;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReader#read()} のためのテスト・メソッド。
     */
    public void testReadTx() {
        Customer c = em.find(Customer.class, 1);
        EntityReader reader = EntityReaderFactory.getEntityReader(c);
        DataSet expected = readXls("TopLinkEntityReaderTest_testReadTx_Expected.xls");
        S2Assert.assertEqualsIgnoreTableOrder(expected, reader.read());
        Enemy e = em.find(Enemy.class, 3);
        reader = EntityReaderFactory.getEntityReader(e);
        expected = readXls("TopLinkEntityReaderTest_testReadTx_Expected2.xls");
        S2Assert.assertEqualsIgnoreTableOrder(expected, reader.read());
    }

}
