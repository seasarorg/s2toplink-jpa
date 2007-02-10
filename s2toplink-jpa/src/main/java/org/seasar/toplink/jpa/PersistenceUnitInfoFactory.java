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
package org.seasar.toplink.jpa;

import javax.persistence.spi.PersistenceUnitInfo;


/**
 * PersistenceUnitInfoの生成、既存のPersitenceUnitInfoオブジェクトへのデータ追加機能を提供します。
 * @author Hidenoshin Yoshida
 *
 */
public interface PersistenceUnitInfoFactory {

    /**
     * 指定されたPersistenceUnit名をキーにクラスパス内のpersistence.xmlを検索し、PersistenceUnitInfoオブジェクトを生成します。 
     * 生成したオブジェクトには、自動登録対象のEntityクラスやマッピングファイル情報が反映されています。
     * @param unitName PersistenceUnit名
     * @return 指定したPersistenceUnit名に対応するPersistenceUnitInfoオブジェクト
     */
    PersistenceUnitInfo getPersistenceUnitInfo(String unitName);
    
    /**
     * PersistenceUnitInfoオブジェクトを渡して、自動登録対象のEntityクラスやマッピングファイル情報を追加します。 
     * @param persistenceUnitInfo PersistenceUnitInfoオブジェクト
     */
    void addAutoDetectResult(PersistenceUnitInfo persistenceUnitInfo);
}
