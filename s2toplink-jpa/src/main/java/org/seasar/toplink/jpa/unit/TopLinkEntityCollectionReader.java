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
package org.seasar.toplink.jpa.unit;

import java.util.Collection;
import java.util.Map;

import org.seasar.toplink.jpa.metadata.TopLinkEntityDesc;

/**
 * @author Hidenoshin Yoshida
 * 
 */
public class TopLinkEntityCollectionReader extends TopLinkEntityReader {

    protected Map<Class<?>, TopLinkEntityDesc> entityDescs;

    protected Class<?> processingClass;

    public TopLinkEntityCollectionReader(final Collection<?> entities,
            final Map<Class<?>, TopLinkEntityDesc> entityDescs) {

        this.entityDescs = entityDescs;

        for (final Object entity : entities) {
            processingClass = entity.getClass();
            setupColumns();
            setupRow(entity);
            processingClass = null;
        }
    }

    @Override
    protected TopLinkEntityDesc getEntityDesc() {
        return entityDescs.get(processingClass);
    }
}
