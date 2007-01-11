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
package org.seasar.toplink.jpa.aop.interceptors;

import oracle.toplink.essentials.ejb.cmp3.EJBQuery;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2TopLinkEntityManagerInterceptorTest extends S2TestCase {
    
    private S2TopLinkEntityManagerInterceptor interceptor;
    
    private MethodInvocation invocation;
    
    private MockInterceptor mockInterceptor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(S2TopLinkEntityManagerInterceptorTest.class.getSimpleName() + ".dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.aop.interceptors.S2TopLinkEntityManagerInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)} のためのテスト・メソッド。
     * @throws Throwable 
     */
    public void testInvoke() throws Throwable {
        Object obj = interceptor.invoke(invocation);
        assertTrue(obj instanceof EJBQuery);
        assertTrue(mockInterceptor.isInvoked("getDatabaseQuery"));
    }

}
