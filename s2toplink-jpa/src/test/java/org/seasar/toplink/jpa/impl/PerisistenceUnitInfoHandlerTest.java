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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.toplink.jpa.PersistenceUnitInfoImpl;
import org.xml.sax.SAXException;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class PerisistenceUnitInfoHandlerTest extends S2TestCase {
	
	private PersistenceUnitInfoHandler persistenceUnitInfoHandler;
    
    
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        include(PerisistenceUnitInfoHandlerTest.class.getSimpleName() + ".dicon");
    }

    public void testPerse() throws ParserConfigurationException, SAXException, IOException {
		
		InputStream is = getClass().getResourceAsStream("/org/seasar/toplink/jpa/impl/persistence.xml");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(is, persistenceUnitInfoHandler);
		
		List<PersistenceUnitInfoImpl> list = persistenceUnitInfoHandler.getPersistenceUnitInfoList();
		
		assertEquals(1, list.size());
		PersistenceUnitInfoImpl info = list.get(0);
		assertEquals("em", info.getPersistenceUnitName());
		assertEquals(PersistenceUnitTransactionType.JTA, info.getTransactionType());
		assertEquals("test", info.getDescription());
		assertEquals("test.Provider", info.getPersistenceProviderClassName());
		assertNotNull(info.getJtaDataSource());
		assertNull(info.getNonJtaDataSource());
		List<String> mappingFileNames = info.getMappingFileNames();
		assertEquals(3, mappingFileNames.size());
		assertTrue(mappingFileNames.contains("test/test1Orm.xml"));
		assertTrue(mappingFileNames.contains("test/test2Orm.xml"));
		assertTrue(mappingFileNames.contains("test/test3Orm.xml"));

		List<String> managedClassNames = info.getManagedClassNames();
		assertEquals(2, managedClassNames.size());
		assertTrue(managedClassNames.contains("test.Test"));
		assertTrue(managedClassNames.contains("test.Test2"));
		assertTrue(info.excludeUnlistedClasses());
		
		Properties prop = info.getProperties();
		assertEquals("org.seasar.toplink.jpa.S2ServerPlatform", prop.getProperty("toplink.target-server"));
		assertEquals("FINE", prop.getProperty("toplink.logging.level"));
	}

}
