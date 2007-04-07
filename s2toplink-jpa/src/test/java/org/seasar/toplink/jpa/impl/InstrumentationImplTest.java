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
package org.seasar.toplink.jpa.impl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import oracle.toplink.essentials.internal.weaving.TopLinkWeaved;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.entity.Customer;
import org.seasar.toplink.jpa.entity.Product;

/**
 * @author taedium
 * 
 */
public class InstrumentationImplTest extends S2TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }

    public void test() throws Exception {
        List<Type> interfaces = Arrays.asList(Customer.class
                .getGenericInterfaces());
        assertFalse(interfaces.contains(TopLinkWeaved.class));

        interfaces = Arrays.asList(Product.class.getGenericInterfaces());
        assertTrue(interfaces.contains(TopLinkWeaved.class));
    }
}
