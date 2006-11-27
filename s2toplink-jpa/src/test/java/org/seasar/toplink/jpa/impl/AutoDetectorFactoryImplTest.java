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
package org.seasar.toplink.jpa.impl;

import java.util.List;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.toplink.jpa.AutoDetectorFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class AutoDetectorFactoryImplTest extends S2TestCase {
    
    private AutoDetectorFactory autoDetectorFactory;

    private ResourceAutoDetector resource1;
    
    private ResourceAutoDetector resource2;
    
    private ClassAutoDetector class1;
    
    private ClassAutoDetector class2;
    
    public void setUpSetMappingFileAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testSetMappingFileAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#setMappingFileAutoDetector(org.seasar.framework.autodetector.ResourceAutoDetector[])} のためのテスト・メソッド。
     */
    public void testSetMappingFileAutoDetector() {
        List<ResourceAutoDetector> list = autoDetectorFactory.getResourceAutoDetectorList(null);
        assertEquals(2, list.size());
        assertEquals(resource1, list.get(0));
        assertEquals(resource2, list.get(1));
    }

    public void setUpAddMappingFileAutoDetectorResourceAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testAddMappingFileAutoDetectorResourceAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#addMappingFileAutoDetector(org.seasar.framework.autodetector.ResourceAutoDetector)} のためのテスト・メソッド。
     */
    public void testAddMappingFileAutoDetectorResourceAutoDetector() {
        List<ResourceAutoDetector> list = autoDetectorFactory.getResourceAutoDetectorList(null);
        assertEquals(2, list.size());
        assertEquals(resource1, list.get(0));
        assertEquals(resource2, list.get(1));
    }

    public void setUpAddMappingFileAutoDetectorStringResourceAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testAddMappingFileAutoDetectorStringResourceAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#addMappingFileAutoDetector(java.lang.String, org.seasar.framework.autodetector.ResourceAutoDetector)} のためのテスト・メソッド。
     */
    public void testAddMappingFileAutoDetectorStringResourceAutoDetector() {
        List<ResourceAutoDetector> list = autoDetectorFactory.getResourceAutoDetectorList(null);
        assertNull(list);
        list = autoDetectorFactory.getResourceAutoDetectorList("test");
        assertEquals(2, list.size());
        assertEquals(resource1, list.get(0));
        assertEquals(resource2, list.get(1));
    }

    public void setUpGetResourceAutoDetectorList() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testGetResourceAutoDetectorList.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#getResourceAutoDetectorList(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetResourceAutoDetectorList() {
        List<ResourceAutoDetector> list = autoDetectorFactory.getResourceAutoDetectorList(null);
        assertNull(list);
        list = autoDetectorFactory.getResourceAutoDetectorList("test1");
        assertEquals(1, list.size());
        assertEquals(resource1, list.get(0));
        list = autoDetectorFactory.getResourceAutoDetectorList("test2");
        assertEquals(1, list.size());
        assertEquals(resource2, list.get(0));
    }

    public void setUpSetPersistenceClassAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testSetPersistenceClassAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#setPersistenceClassAutoDetector(org.seasar.framework.autodetector.ClassAutoDetector[])} のためのテスト・メソッド。
     */
    public void testSetPersistenceClassAutoDetector() {
        List<ClassAutoDetector> list = autoDetectorFactory.getClassAutoDetectorList(null);
        assertEquals(2, list.size());
        assertEquals(class1, list.get(0));
        assertEquals(class2, list.get(1));
    }

    public void setUpAddPersistenceClassAutoDetectorClassAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testAddPersistenceClassAutoDetectorClassAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#addPersistenceClassAutoDetector(org.seasar.framework.autodetector.ClassAutoDetector)} のためのテスト・メソッド。
     */
    public void testAddPersistenceClassAutoDetectorClassAutoDetector() {
        List<ClassAutoDetector> list = autoDetectorFactory.getClassAutoDetectorList(null);
        assertEquals(2, list.size());
        assertEquals(class1, list.get(0));
        assertEquals(class2, list.get(1));
    }

    public void setUpAddPersistenceClassAutoDetectorStringClassAutoDetector() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testAddPersistenceClassAutoDetectorStringClassAutoDetector.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#addPersistenceClassAutoDetector(java.lang.String, org.seasar.framework.autodetector.ClassAutoDetector)} のためのテスト・メソッド。
     */
    public void testAddPersistenceClassAutoDetectorStringClassAutoDetector() {
        List<ClassAutoDetector> list = autoDetectorFactory.getClassAutoDetectorList(null);
        assertNull(list);
        list = autoDetectorFactory.getClassAutoDetectorList("test");
        assertEquals(2, list.size());
        assertEquals(class1, list.get(0));
        assertEquals(class2, list.get(1));
    }

    public void setUpGetClassAutoDetectorList() {
        include(AutoDetectorFactoryImplTest.class.getSimpleName() + "_testGetClassAutoDetectorList.dicon");
    }
    /**
     * {@link org.seasar.toplink.jpa.impl.AutoDetectorFactoryImpl#getClassAutoDetectorList(java.lang.String)} のためのテスト・メソッド。
     */
    public void testGetClassAutoDetectorList() {
        List<ClassAutoDetector> list = autoDetectorFactory.getClassAutoDetectorList(null);
        assertNull(list);
        list = autoDetectorFactory.getClassAutoDetectorList("test1");
        assertEquals(1, list.size());
        assertEquals(class1, list.get(0));
        list = autoDetectorFactory.getClassAutoDetectorList("test2");
        assertEquals(1, list.size());
        assertEquals(class2, list.get(0));
    }

}
