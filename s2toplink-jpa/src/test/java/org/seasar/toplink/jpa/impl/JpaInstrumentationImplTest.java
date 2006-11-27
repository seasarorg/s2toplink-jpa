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
package org.seasar.toplink.jpa.impl;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.spi.ClassTransformer;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.JpaInstrumentation;
import org.seasar.toplink.jpa.entity.Customer;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class JpaInstrumentationImplTest extends S2TestCase {
    
    private JpaInstrumentation instrumentation;
    
    private ClassTransformer transformer;
    
    private ClassLoader classLoader;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include(JpaInstrumentationImplTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.impl.JpaInstrumentationImpl#addTransformer(javax.persistence.spi.ClassTransformer, java.lang.ClassLoader)} のためのテスト・メソッド。
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void testAddTransformer() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        instrumentation.addTransformer(transformer, classLoader);
        Field field = ClassLoader.class.getDeclaredField("classes");
        field.setAccessible(true);
        List classes = List.class.cast(field.get(classLoader));
        assertEquals(1, classes.size());
        assertEquals(Customer.class.getName(), Class.class.cast(classes.get(0)).getName());
    }

}
