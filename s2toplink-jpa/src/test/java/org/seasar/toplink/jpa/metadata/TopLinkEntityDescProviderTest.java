package org.seasar.toplink.jpa.metadata;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;

import example.entity.TestC;

public class TopLinkEntityDescProviderTest extends S2TestCase {

    private EntityDescProvider entityDescProvider;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(TopLinkEntityDescProviderTest.class.getSimpleName() + ".dicon");
    }


    public void testCreateEntityDescTx() {
        EntityDesc desc = entityDescProvider.createEntityDesc(TestC.class);
        assertEquals(TestC.class, desc.getEntityClass());
        assertEquals("Test_C", desc.getEntityName());
        for (AttributeDesc aDesc : desc.getAttributeDescs()) {
            System.out.println(aDesc.getName());
            System.out.println(aDesc.getType());
            System.out.println(aDesc.isId());
            System.out.println(aDesc.isVersion());
        }
        System.out.println();
        for (String name : desc.getAttributeNames()) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println(desc.getIdAttributeDesc().getName());
        System.out.println(desc.getIdAttributeDesc().getType());
        System.out.println(desc.getIdAttributeDesc().isId());
        System.out.println(desc.getIdAttributeDesc().isVersion());
    }

}
