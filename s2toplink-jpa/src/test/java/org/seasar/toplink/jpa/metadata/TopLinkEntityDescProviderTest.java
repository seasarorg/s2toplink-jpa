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
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.toplink.jpa.entity.Customer;
import org.seasar.toplink.jpa.entity.Product;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityDescProviderTest extends S2TestCase {
    
    private EntityDescProvider provider;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.metadata.TopLinkEntityDescProvider#createEntityDesc(java.lang.Class)} のためのテスト・メソッド。
     */
    public void testCreateEntityDesc() {
        EntityDesc desc = provider.createEntityDesc(Integer.class);
        assertNull(desc);
        desc = provider.createEntityDesc(Customer.class);
        assertNotNull(desc);
        desc = provider.createEntityDesc(Product.class);
        assertNotNull(desc);
    }

}
