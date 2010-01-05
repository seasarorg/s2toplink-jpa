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
package org.seasar.toplink.jpa.metadata;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;
import oracle.toplink.essentials.threetier.ServerSession;

import org.junit.runner.RunWith;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.unit.Seasar2;
import org.seasar.toplink.jpa.entity.Customer;
import org.seasar.toplink.jpa.entity.Enemy;
import org.seasar.toplink.jpa.entity.Product;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 *
 */
@RunWith(Seasar2.class)
public class TopLinkEntityDescTest {
    
    private EntityManagerFactory emf;
    
    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#TopLinkEntityDesc(java.lang.Class, oracle.toplink.essentials.threetier.ServerSession)} のためのテスト・メソッド。
     */
    public void testTopLinkEntityDesc() {
        ServerSession serverSession = EntityManagerFactoryImpl.class.cast(emf).getServerSession();
        TopLinkEntityDesc desc = new TopLinkEntityDesc(Customer.class, serverSession);
        assertEquals(serverSession, desc.getServerSession());
        assertEquals(serverSession.getDescriptor(Customer.class), desc.classDescriptor);
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getAttributeDesc(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getAttributeDesc("id").getName(), "id");
        assertEquals(desc.getAttributeDesc("name").getName(), "name");
        assertEquals(desc.getAttributeDesc("address").getName(), "address");
        assertEquals(desc.getAttributeDesc("phone").getName(), "phone");
        assertEquals(desc.getAttributeDesc("age").getName(), "age");
        assertEquals(desc.getAttributeDesc("birthday").getName(), "birthday");
        assertEquals(desc.getAttributeDesc("sex").getName(), "sex");
        assertEquals(desc.getAttributeDesc("version").getName(), "version");
        assertEquals(desc.getAttributeDesc("products").getName(), "products");
        assertNull(desc.getAttributeDesc(null));
        assertNull(desc.getAttributeDesc(""));
        
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getAttributeDesc("id").getName(), "id");
        assertEquals(desc.getAttributeDesc("name").getName(), "name");
        assertEquals(desc.getAttributeDesc("version").getName(), "version");
        assertEquals(desc.getAttributeDesc("customer").getName(), "customer");
        assertNull(desc.getAttributeDesc(null));
        assertNull(desc.getAttributeDesc(""));
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getAttributeDescs()} のためのテスト・メソッド。
     */
    public void testGetAttributeDescs() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(9, desc.getAttributeDescs().length);
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(4, desc.getAttributeDescs().length);
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getAttributeNames()} のためのテスト・メソッド。
     */
    public void testGetAttributeNames() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        String[] names = desc.getAttributeNames();
        assertEquals(9, names.length);
        List<String> list = Arrays.asList(names);
        list.contains("id");
        list.contains("name");
        list.contains("address");
        list.contains("phone");
        list.contains("age");
        list.contains("birthday");
        list.contains("sex");
        list.contains("version");
        list.contains("products");
        desc = EntityDescFactory.getEntityDesc(Product.class);
        names = desc.getAttributeNames();
        assertEquals(4, names.length);
        list = Arrays.asList(names);
        list.contains("id");
        list.contains("name");
        list.contains("version");
        list.contains("customer");
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getEntityClass()} のためのテスト・メソッド。
     */
    public void testGetEntityClass() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getEntityClass(), Customer.class);
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getEntityClass(), Product.class);
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getEntityName()} のためのテスト・メソッド。
     */
    public void testGetEntityName() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getEntityName(), Customer.class.getSimpleName());
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getEntityName(), Product.class.getSimpleName());
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getIdAttributeDesc()} のためのテスト・メソッド。
     */
    public void testGetIdAttributeDesc() {
        EntityDesc desc = EntityDescFactory.getEntityDesc(Customer.class);
        assertEquals(desc.getIdAttributeDesc(), desc.getAttributeDesc("id"));
        desc = EntityDescFactory.getEntityDesc(Product.class);
        assertEquals(desc.getIdAttributeDesc(), desc.getAttributeDesc("id"));
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getTableNames()} のためのテスト・メソッド。
     */
    public void testGetTableNames() {
        TopLinkEntityDesc desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Customer.class));
        assertEquals(desc.getTableNames(), Arrays.asList(new String[]{"CUSTOMER"}));
        desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Product.class));
        assertEquals(desc.getTableNames(), Arrays.asList(new String[]{"PRODUCT"}));
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getServerSession()} のためのテスト・メソッド。
     */
    public void testGetServerSession() {
        TopLinkEntityDesc desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Customer.class));
        assertNotNull(desc.getServerSession());
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#hasDiscriminatorColumn()} のためのテスト・メソッド。
     */
    public void testHasDiscriminatorColumn() {
        TopLinkEntityDesc desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Product.class));
        assertFalse(desc.hasDiscriminatorColumn());
        desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Enemy.class));
        assertTrue(desc.hasDiscriminatorColumn());
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDesc#getInheritancePolicy()} のためのテスト・メソッド。
     */
    public void testGetInheritancePolicy() {
        TopLinkEntityDesc desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Product.class));
        assertNull(desc.getInheritancePolicy());
        desc = TopLinkEntityDesc.class.cast(EntityDescFactory.getEntityDesc(Enemy.class));
        assertNotNull(desc.getInheritancePolicy());
    }

}
