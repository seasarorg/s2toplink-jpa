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

import org.seasar.framework.util.ChildFirstClassLoader;

/**
 * ChildFirstClassLoaderを継承した、エンハンス対象Entityクラス情報を取得する為のClassLoaderです。
 * 
 * @author Hidenoshin Yoshida
 */
public class S2TopLinkTempClassLoader extends ChildFirstClassLoader {

    /**
     * エンハンス対象Entityクラス名のSet
     */
    protected Set<String> tempClassNameSet;

    /**
     * コンストラクタ
     * 
     * @param classLoader
     *            設定するClassLoader
     * @param tempClassNameSet
     *            このクラスローダによるロード対象クラスSet
     */
    public S2TopLinkTempClassLoader(ClassLoader classLoader,
            Set<String> tempClassNameSet) {
        super(classLoader);
        this.tempClassNameSet = tempClassNameSet;
    }

    /**
     * @see org.seasar.framework.util.ChildFirstClassLoader#isStystemClass(java.lang.String)
     */
    @Override
    protected boolean isExcludedClass(String className) {
        boolean ret = super.isExcludedClass(className);

        if (!ret && tempClassNameSet != null) {
            ret = !tempClassNameSet.contains(className);
        }
        return ret;
    }

}
