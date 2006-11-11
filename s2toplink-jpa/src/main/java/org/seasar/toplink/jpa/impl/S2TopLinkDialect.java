package org.seasar.toplink.jpa.impl;

import java.sql.Connection;
import java.util.Properties;

import javax.persistence.EntityManager;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.jpa.DialectManager;

public class S2TopLinkDialect implements Dialect {

    @Binding(bindingType = BindingType.MUST)
    protected DialectManager dialectManager;

    @InitMethod
    public void initialize() {
        dialectManager.addDialect(oracle.toplink.essentials.ejb.cmp3.EntityManager.class, this);
    }

    @DestroyMethod
    public void destroy() {
        dialectManager.removeDialect(oracle.toplink.essentials.ejb.cmp3.EntityManager.class);
    }

    public Connection getConnection(EntityManager em) {
        if (em.getDelegate() instanceof oracle.toplink.essentials.ejb.cmp3.EntityManager) {
            oracle.toplink.essentials.ejb.cmp3.EntityManager toplinkEm = oracle.toplink.essentials.ejb.cmp3.EntityManager.class.cast(em.getDelegate());
            return toplinkEm.getServerSession().getLogin().getConnector().connect(new Properties());
        }
        return null;
    }

}
