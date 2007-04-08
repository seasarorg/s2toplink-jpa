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

import javax.persistence.spi.PersistenceUnitInfo;

import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * 永続クラス自動登録用ClassHandlerです。
 * 
 * @author taedium
 * 
 */
public class PersistenceClassHandler implements ClassHandler {

    private static final Logger logger = Logger
            .getLogger(S2JavaSECMPInitializer.class);

    /**
     * 永続ユニット情報です。
     */
    protected PersistenceUnitInfo unitInfo;

    /**
     * コンストラクタ
     * 
     * @param unitInfo
     *            永続ユニット情報
     */
    public PersistenceClassHandler(final PersistenceUnitInfo unitInfo) {
        this.unitInfo = unitInfo;
    }

    /**
     * @see org.seasar.framework.util.ClassTraversal.ClassHandler#processClass(java.lang.String,
     *      java.lang.String)
     */
    public void processClass(final String packageName,
            final String shortClassName) {
        final String className = ClassUtil.concatName(packageName,
                shortClassName);
        if (logger.isDebugEnabled()) {
            logger.log("DTLJPA0001", new Object[] { className,
                    unitInfo.getPersistenceUnitName() });
        }
        unitInfo.getManagedClassNames().add(className);
    }
}