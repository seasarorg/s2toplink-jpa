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

import java.util.Set;

import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.convention.NamingConvention;

/**
 * HotdeployClassLoaderを継承した、エンハンス対象Entityクラス情報を取得する為のClassLoaderです。
 * @author Hidenoshin Yoshida
 */
public class S2TopLinkTempClassLoader extends HotdeployClassLoader {
    
    /**
     * エンハンス対象Entityクラス名のSet
     */
    protected Set<String> tempClassNameSet;
    
    /**
     * コンストラクタ
     * @param namingConvention 設定するNamingConvention
     */
    public S2TopLinkTempClassLoader(NamingConvention namingConvention) {
        super(Thread.currentThread().getContextClassLoader(), namingConvention);
    }

    /**
     * コンストラクタ
     * @param classLoader 設定するClassLoader
     * @param namingConvention 設定するNamingConvention
     */
    public S2TopLinkTempClassLoader(ClassLoader classLoader,
            NamingConvention namingConvention) {
        super(classLoader, namingConvention);
    }

    /**
     * エンハンス対象Entityクラス名のSetを設定します。
     * @param tempClassNameSet 設定するSet
     */
    public void setTempClassNameSet(Set<String> tempClassNameSet) {
        this.tempClassNameSet = tempClassNameSet;
    }

    /**
     * NamingConventionによる判定に加え、tempClassNameSetにクラス名が存在した場合もtrueを返します。
     * @see org.seasar.framework.container.hotdeploy.HotdeployClassLoader#isTargetClass(java.lang.String)
     */
    @Override
    protected boolean isTargetClass(String className) {
        boolean ret = super.isTargetClass(className);
        if (!ret && tempClassNameSet != null) {
            ret = tempClassNameSet.contains(className);
        }
        return ret;
    }

}
