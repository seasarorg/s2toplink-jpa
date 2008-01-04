/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import org.seasar.framework.jpa.metadata.EntityDescProvider;

/**
 * TopLink Essentials用のEntityDescProvider実装です。
 * 
 * @author Hidenoshin Yoshida
 */
public class TopLinkEntityDescProvider implements EntityDescProvider {

    /**
     * コンストラクタ
     * 
     */
    public TopLinkEntityDescProvider() {
    }

    public TopLinkEntityDesc createEntityDesc(EntityManagerFactory emf,
            Class<?> entityClass) {
        ServerSession serverSession = EntityManagerFactoryImpl.class.cast(emf)
                .getServerSession();
        if (serverSession.getDescriptor(entityClass) != null) {
            return new TopLinkEntityDesc(entityClass, serverSession);
        }
        return null;
    }

}
