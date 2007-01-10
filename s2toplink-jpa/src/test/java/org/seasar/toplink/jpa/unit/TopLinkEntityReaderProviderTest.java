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

import java.util.GregorianCalendar;
import java.util.List;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.unit.S2Assert;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.entity.Dangeon;
import org.seasar.toplink.jpa.entity.Enemy;
import org.seasar.toplink.jpa.entity.Sex;

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
        enemy.setId(3);
        enemy.setName("Ganon");
        enemy.setAddress("Darkness");
        enemy.setPhone("000-0000-0000");
        enemy.setAge(15);
        enemy.setBirthday(new GregorianCalendar(1998, 0, 1).getTime());
        enemy.setSex(Sex.MAN);
        enemy.setVersion(0);
        Dangeon dangeon = new Dangeon();
        dangeon.setDangeonName("Pyramid of Power");
        dangeon.setDangeonLevel(10);
        enemy.setDangeon(dangeon);
        TopLinkEntityReader reader = topLinkEntityReaderProvider.createEntityReader(enemy);
        assertEquals(EntityDescFactory.getEntityDesc(Enemy.class), reader.getEntityDesc());
        DataSet expected = readXls(TopLinkEntityReaderProviderTest.class.getSimpleName() + "_testCreateEntityReaderObject_Expected.xls");
        S2Assert.assertEqualsIgnoreTableOrder(null, expected, reader.read());
    }

    /**
     * {@link org.seasar.toplink.jpa.unit.TopLinkEntityReaderProvider#createEntityReader(java.util.Collection)} のためのテスト・メソッド。
     */
    public void testCreateEntityReaderCollectionOfQ() {
        List<Enemy> list = CollectionsUtil.newArrayList();
        Enemy enemy = new Enemy();
        enemy.setId(3);
        enemy.setName("Ganon");
        enemy.setAddress("Darkness");
        enemy.setPhone("000-0000-0000");
        enemy.setAge(15);
        enemy.setBirthday(new GregorianCalendar(1998, 0, 1).getTime());
        enemy.setSex(Sex.MAN);
        enemy.setVersion(0);
        Dangeon dangeon = new Dangeon();
        dangeon.setDangeonName("Pyramid of Power");
        dangeon.setDangeonLevel(10);
        enemy.setDangeon(dangeon);
        list.add(enemy);
        Enemy enemy2 = new Enemy();
        enemy2.setId(4);
        enemy2.setName("Agahnim");
        enemy2.setAddress("Darkness");
        enemy2.setPhone("000-0000-0000");
        enemy2.setAge(70);
        enemy2.setBirthday(new GregorianCalendar(1920, 0, 1).getTime());
        enemy2.setSex(Sex.MAN);
        enemy2.setVersion(0);
        Dangeon dangeon2 = new Dangeon();
        dangeon2.setDangeonName("Ganon's Tower");
        dangeon2.setDangeonLevel(10);
        enemy2.setDangeon(dangeon2);
        list.add(enemy2);
        TopLinkEntityCollectionReader reader = topLinkEntityReaderProvider.createEntityReader(list);
        DataSet expected = readXls(TopLinkEntityReaderProviderTest.class.getSimpleName() + "_testCreateEntityReaderCollectionOfQ_Expected.xls");
        S2Assert.assertEqualsIgnoreTableOrder(null, expected, reader.read());
    }

}
