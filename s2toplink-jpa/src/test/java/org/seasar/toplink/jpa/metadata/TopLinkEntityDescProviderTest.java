/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.persistence.EntityManagerFactory;

import org.junit.runner.RunWith;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.unit.Seasar2;
import org.seasar.toplink.jpa.entity.Customer;
import org.seasar.toplink.jpa.entity.Product;

import static junit.framework.Assert.*;

/**
 * @author Hidenoshin Yoshida
 * 
 */
@RunWith(Seasar2.class)
public class TopLinkEntityDescProviderTest {

    private EntityDescProvider provider;

    private EntityManagerFactory emf;

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDescProvider#createEntityDesc(java.lang.Class)}
     * のためのテスト・メソッド。
     */
    public void testCreateEntityDesc() {
        EntityDesc desc = provider.createEntityDesc(emf, Integer.class);
        assertNull(desc);
        desc = provider.createEntityDesc(emf, Customer.class);
        assertNotNull(desc);
        desc = provider.createEntityDesc(emf, Product.class);
        assertNotNull(desc);
    }

}
