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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.AutoDetectorFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class AutoDetectorFactoryImpl implements AutoDetectorFactory {

    /**
     * PersistenceUnit名をキー、対応するResourceAutoDetectorのListを値に持つMap
     */
    protected Map<String, List<ResourceAutoDetector>> mappingFileAutoDetectors = CollectionsUtil
            .newHashMap();

    /**
     * PersistenceUnit名をキー、対応するClassAutoDetectorのListを値に持つMap
     */
    protected Map<String, List<ClassAutoDetector>> persistenceClassAutoDetectors = CollectionsUtil
            .newHashMap();

    /**
     * ResourceAutoDetectorの配列を受け取り、キー値nullで登録します。
     * @param resourceAutoDetectors ResourceAutoDetectorの配列
     */
    public void setMappingFileAutoDetector(
            final ResourceAutoDetector[] resourceAutoDetectors) {
        for (final ResourceAutoDetector detector : resourceAutoDetectors) {
            addMappingFileAutoDetector(detector);
        }
    }

    /**
     * ResourceAutoDetectorオブジェクトを受け取り、キー値nullで登録します。
     * @param detector ResourceAutoDetectorオブジェクト
     */
    public void addMappingFileAutoDetector(final ResourceAutoDetector detector) {
        addMappingFileAutoDetector(null, detector);
    }

    /**
     * ResourceAutoDetectorオブジェクトを、指定されたPersistenceUnit名で登録します。
     * @param unitName PersistenceUnit名。null値可。
     * @param detector ResourceAutoDetectorオブジェクト
     */
    public void addMappingFileAutoDetector(final String unitName,
            final ResourceAutoDetector detector) {
        if (!mappingFileAutoDetectors.containsKey(unitName)) {
            mappingFileAutoDetectors.put(unitName,
                    new ArrayList<ResourceAutoDetector>());
        }
        mappingFileAutoDetectors.get(unitName).add(detector);
    }

    /**
     * @see org.seasar.toplink.jpa.AutoDetectorFactory#getResourceAutoDetectorList(java.lang.String)
     */
    public List<ResourceAutoDetector> getResourceAutoDetectorList(
            String unitName) {
        return mappingFileAutoDetectors.get(unitName);
    }

    /**
     * ClassAutoDetectorの配列を受け取り、キー値nullで登録します。
     * @param classAutoDetectors ClassAutoDetectorの配列
     */
    public void setPersistenceClassAutoDetector(
            final ClassAutoDetector[] classAutoDetectors) {
        for (final ClassAutoDetector detector : classAutoDetectors) {
            addPersistenceClassAutoDetector(detector);
        }
    }

    /**
     * ClassAutoDetectorオブジェクトを受け取り、キー値nullで登録します。
     * @param detector ClassAutoDetectorオブジェクト
     */
    public void addPersistenceClassAutoDetector(final ClassAutoDetector detector) {
        addPersistenceClassAutoDetector(null, detector);
    }

    /**
     * ClassAutoDetectorオブジェクトを、指定されたPersistenceUnit名で登録します。
     * @param unitName PersistenceUnit名。null値可。
     * @param detector ClassAutoDetectorオブジェクト
     */
    public void addPersistenceClassAutoDetector(final String unitName,
            final ClassAutoDetector detector) {
        if (!persistenceClassAutoDetectors.containsKey(unitName)) {
            persistenceClassAutoDetectors.put(unitName,
                    new ArrayList<ClassAutoDetector>());
        }
        persistenceClassAutoDetectors.get(unitName).add(detector);
    }

    /**
     * @see org.seasar.toplink.jpa.AutoDetectorFactory#getClassAutoDetectorList(java.lang.String)
     */
    public List<ClassAutoDetector> getClassAutoDetectorList(
            String unitName) {
        return persistenceClassAutoDetectors.get(unitName);
    }


}
