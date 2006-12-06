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

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.metadata.EntityDescProvider;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityDescProvider implements EntityDescProvider {
    
    protected EntityManagerFactoryImpl emf;
    
    protected ServerSession serverSession;
    
    
    
    @SuppressWarnings("unchecked")
    public TopLinkEntityDescProvider(EntityManagerFactory entityManagerFactory) {
        this.emf = EntityManagerFactoryImpl.class.cast(entityManagerFactory);
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
        if (serverSession == null) {
            serverSession = emf.getServerSession();
        }
        if (serverSession.getDescriptor(entityClass) != null) {
            return new TopLinkEntityDesc(entityClass, serverSession);
        }
        return null;
    }

}
