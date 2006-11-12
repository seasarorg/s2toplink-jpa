package org.seasar.toplink.jpa.aop.interceptors;

import oracle.toplink.essentials.ejb.cmp3.EJBQuery;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

public class S2TopLinkEntityManagerInterceptor extends AbstractInterceptor {

    /**
     * 
     */
    private static final long serialVersionUID = -4065883918496694771L;


    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object ret = invocation.proceed();
        if (ret instanceof EJBQuery) {
            EJBQuery query = EJBQuery.class.cast(ret);
            query.getDatabaseQuery();
        }
        return ret;
    }

}
