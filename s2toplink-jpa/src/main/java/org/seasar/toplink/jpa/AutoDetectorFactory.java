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

import java.util.List;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;

/**
 * Entityクラスを自動登録するClassAutoDetectorや、マッピングファイルを自動登録するResourceAutoDetectorを生成します。
 * @author Hidenoshin Yoshida
 *
 */
public interface AutoDetectorFactory {
    
    /**
     * 指定されたPersistenceUnit名に対応するResourceAutoDetectorオブジェクトのListを返します。
     * @param unitName PersistenceUnit名
     * @return unitNameで登録されているResourceAutoDetectorのListを返します。 nullを指定したときは、null値で登録されたResourceAutoDetectorのListを返します。 
     * 
     */
    List<ResourceAutoDetector> getResourceAutoDetectorList(String unitName);
    
    /**
     * 指定されたPersistenceUnit名に対応するClassAutoDetectorオブジェクトのListを返します。
     * @param unitName PersistenceUnit名
     * @return unitNameで登録されているClassAutoDetectorのListを返します。 nullを指定したときは、null値で登録されたClassAutoDetectorのListを返します。 
     */
    List<ClassAutoDetector> getClassAutoDetectorList(String unitName);
}
