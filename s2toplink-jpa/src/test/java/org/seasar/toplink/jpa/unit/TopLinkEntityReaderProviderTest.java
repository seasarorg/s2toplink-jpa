package org.seasar.toplink.jpa.unit;

import java.util.Calendar;

import javax.persistence.EntityManager;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;

import example.entity.TestC;
import example.entity.TestCId;

public class TopLinkEntityReaderProviderTest extends S2TestCase {
    
    private TopLinkEntityReaderProvider provider;

    private EntityManager em;
    
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
