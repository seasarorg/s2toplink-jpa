package org.seasar.toplink.jpa.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;
import org.seasar.toplink.jpa.PersistenceUnitInfoImpl;
import org.xml.sax.SAXException;

@RunWith(Seasar2.class)
public class PerisistenceUnitInfoHandlerTest {
	
	private PersistenceUnitInfoHandler persistenceUnitInfoHandler;
	
	public void testPerse() throws ParserConfigurationException, SAXException, IOException {
		
		InputStream is = getClass().getResourceAsStream("/org/seasar/toplink/jpa/impl/persistence.xml");
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
//		persistenceUnitInfoHandler = (PersistenceUnitInfoHandler) getComponent(PersistenceUnitInfoHandler.class);
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
