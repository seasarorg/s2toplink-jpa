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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.toplink.jpa.PersistenceUnitInfoCreator;
import org.seasar.toplink.jpa.PersistenceUnitInfoImpl;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class PersistenceUnitInfoHandler extends DefaultHandler {
	
	private List<PersistenceUnitInfoImpl> persistenceUnitInfoList;
	
	private PersistenceUnitInfoImpl persistenceUnitInfo;

	private PersistenceUnitInfoCreator persistenceUnitInfoCreator;
	
	private Context context;
	
	protected StringBuilder characters;
		
	public void setPersistenceUnitInfoCreator(
			PersistenceUnitInfoCreator persistenceUnitInfoCreator) {
		this.persistenceUnitInfoCreator = persistenceUnitInfoCreator;
	}

	@Binding(bindingType = BindingType.MUST)
	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	@Binding(bindingType = BindingType.MAY)
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
	}

	public List<PersistenceUnitInfoImpl> getPersistenceUnitInfoList() {
		return persistenceUnitInfoList;
	}

	@Override
	public void startDocument() throws SAXException {
		persistenceUnitInfoList = new ArrayList<PersistenceUnitInfoImpl>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		characters = new StringBuilder();
		if ("persistence-unit".equals(qName)) {
			persistenceUnitInfo = persistenceUnitInfoCreator.createPersistenceUnitInfo();
			String name = attributes.getValue("name");
			if (name != null) {
				persistenceUnitInfo.setPersistenceUnitName(name);
			}
			String transactionType = attributes.getValue("transaction-type");
			if (transactionType != null) {
				persistenceUnitInfo.setTransactionType(PersistenceUnitTransactionType.valueOf(transactionType));
			}
		} else if ("description".equals(qName)) {
			characters = new StringBuilder();
		} else if ("provider".equals(qName)) {
			characters = new StringBuilder();
		} else if ("jta-data-source".equals(qName)) {
			characters = new StringBuilder();
		} else if ("non-jta-data-source".equals(qName)) {
			characters = new StringBuilder();
		} else if ("mapping-file".equals(qName)) {
			characters = new StringBuilder();
		} else if ("jar-file".equals(qName)) {
			characters = new StringBuilder();
		} else if ("class".equals(qName)) {
			characters = new StringBuilder();
		} else if ("exclude-unlisted-classes".equals(qName)) {
			characters = new StringBuilder();
		} else if ("property".equals(qName)) {
			String name = attributes.getValue("name");
			String value = attributes.getValue("value");
			persistenceUnitInfo.getProperties().setProperty(name, value);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("persistence-unit".equals(qName)) {
			persistenceUnitInfoList.add(persistenceUnitInfo);
		} else if ("description".equals(qName)) {
			persistenceUnitInfo.setDescription(getCharacters());
		} else if ("provider".equals(qName)) {
			persistenceUnitInfo.setPersistenceProviderClassName(getCharacters());
		} else if ("jta-data-source".equals(qName)) {
			String name = getCharacters();
			if (name != null) {
				try {
					persistenceUnitInfo.setJtaDataSource((DataSource) context.lookup(name));
				} catch (NamingException e) {
					throw new PersistenceException(e);
				}
			}
		} else if ("non-jta-data-source".equals(qName)) {
			String name = getCharacters();
			if (name != null) {
				try {
					persistenceUnitInfo.setJtaDataSource((DataSource) context.lookup(name));
				} catch (NamingException e) {
					throw new PersistenceException(e);
				}
			}
		} else if ("mapping-file".equals(qName)) {
			String name = getCharacters();
			if (name != null) {
				persistenceUnitInfo.getMappingFileNames().add(name);
			}
		} else if ("jar-file".equals(qName)) {
			String name = getCharacters();
			if (name != null) {
	            URL url = null;
	            try{
	                url = new URL(name);
	            } catch (MalformedURLException exc){
	                try{
	                    url = new URL("file:///" + name);
	                } catch (MalformedURLException exception){
	                }
	            }
	            if (url != null) {
		            persistenceUnitInfo.getJarFileUrls().add(url);
	            }
			}
		} else if ("class".equals(qName)) {
			String name = getCharacters();
			if (name != null) {
				persistenceUnitInfo.getManagedClassNames().add(name);
			}
		} else if ("exclude-unlisted-classes".equals(qName)) {
			persistenceUnitInfo.setExcludeUnlistedClasses(Boolean.parseBoolean(getCharacters()));
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (characters != null) {
			characters.append(ch, start, length);
		}
	}
	
	protected String getCharacters() {
		String name = null;
		if (characters != null) {
			name = characters.toString().trim();
			if (name.length() == 0) {
				name = null;
			}
		}
		return name;
	}


}
