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
package org.seasar.toplink.jpa.unit;

import java.util.List;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.entity.Enemy;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityReaderProviderTest extends S2TestCase {
    
    public static void main(String[] args) {
        
    }
    
    private TopLinkEntityReaderProvider topLinkEntityReaderProvider;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        include("jpa.dicon");
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReaderProvider#createEntityReader(java.lang.Object)} のためのテスト・メソッド。
     */
    public void testCreateEntityReaderObject() {
        Enemy enemy = new Enemy();
        TopLinkEntityReader reader = topLinkEntityReaderProvider.createEntityReader(enemy);
        assertEquals(EntityDescFactory.getEntityDesc(Enemy.class), reader.getEntityDesc());
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReaderProvider#createEntityReader(java.util.Collection)} のためのテスト・メソッド。
     */
    public void testCreateEntityReaderCollectionOfQ() {
        List<Enemy> list = CollectionsUtil.newArrayList();
        list.add(new Enemy());
        list.add(new Enemy());
        TopLinkEntityCollectionReader reader = topLinkEntityReaderProvider.createEntityReader(list);
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReaderProvider#flatten(java.util.Collection)} のためのテスト・メソッド。
     */
    public void testFlatten() {
        fail("まだ実装されていません。");
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReaderProvider#getEntityDesc(java.lang.Class)} のためのテスト・メソッド。
     */
    public void testGetEntityDesc() {
        fail("まだ実装されていません。");
    }

}
