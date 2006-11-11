package org.seasar.toplink.jpa.metadata;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.metadata.EntityDescProvider;


public class TopLinkEntityDescProvider implements EntityDescProvider {
    
    protected EntityManagerFactoryImpl entityManagerFactoryImpl;
    
    @SuppressWarnings("unchecked")
    public TopLinkEntityDescProvider(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactoryImpl = EntityManagerFactoryImpl.class.cast(entityManagerFactory);
    }

    @InitMethod
    public void register() {
        EntityDescFactory.addProvider(this);
    }

    @DestroyMethod
    public void unregister() {
        EntityDescFactory.removeProvider(this);
    }

    public EntityDesc createEntityDesc(Class<?> entityClass) {
        return new TopLinkEntityDesc(entityClass, entityManagerFactoryImpl);
    }

}
