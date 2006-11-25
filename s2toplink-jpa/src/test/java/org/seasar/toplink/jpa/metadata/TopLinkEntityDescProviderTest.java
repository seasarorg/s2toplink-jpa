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
package org.seasar.toplink.jpa.metadata;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;

import example.entity.TestC;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityDescProviderTest extends S2TestCase {

    private EntityDescProvider entityDescProvider;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(TopLinkEntityDescProviderTest.class.getSimpleName() + ".dicon");
    }


    public void testCreateEntityDescTx() {
        EntityDesc desc = entityDescProvider.createEntityDesc(TestC.class);
//        assertEquals(TestD.class, desc.getEntityClass());
//        assertEquals("TestD", desc.getEntityName());
        for (AttributeDesc aDesc : desc.getAttributeDescs()) {
            System.out.println("NAME:" + aDesc.getName());
            System.out.println("TYPE:" + aDesc.getType());
            System.out.println("ID:" + aDesc.isId());
            System.out.println("VERSION:" + aDesc.isVersion());
            System.out.println("ELEMENT_TYPE:" + aDesc.getElementType());
            System.out.println("ASSOCIATION:" + aDesc.isAssociation());
            System.out.println("COLLECTION:" + aDesc.isCollection());
            System.out.println("COMPONENT:" + aDesc.isComponent());
            System.out.println("SQLTYPE:" + aDesc.getSqlType());
            System.out.println();
        }
        for (String name : desc.getAttributeNames()) {
            System.out.println(name);
        }
        System.out.println();
        System.out.println(desc.getIdAttributeDesc().getName());
        System.out.println(desc.getIdAttributeDesc().getType());
        System.out.println(desc.getIdAttributeDesc().isId());
        System.out.println(desc.getIdAttributeDesc().isVersion());
        System.out.println(desc.getIdAttributeDesc().getElementType());
        System.out.println(desc.getIdAttributeDesc().isAssociation());
    }

}
