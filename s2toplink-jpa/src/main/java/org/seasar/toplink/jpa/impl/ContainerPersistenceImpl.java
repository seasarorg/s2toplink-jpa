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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.xml.parsers.SAXParser;

import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.toplink.jpa.ContainerPersistence;
import org.seasar.toplink.jpa.PersistenceUnitInfoImpl;
import org.xml.sax.SAXException;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class ContainerPersistenceImpl implements
		ContainerPersistence {

	private Map<String, PersistenceUnitInfo> puiMap = new HashMap<String, PersistenceUnitInfo>();

	private Map<String, PersistenceProvider> providers = new HashMap<String, PersistenceProvider>();

	private SAXParser parser;
	
	private PersistenceUnitInfoHandler persistenceUnitInfoHandler;
	
	public void setParser(SAXParser parser) {
		this.parser = parser;
	}

	public void setPersistenceUnitInfoHandler(
			PersistenceUnitInfoHandler persistenceUnitInfoHandler) {
		this.persistenceUnitInfoHandler = persistenceUnitInfoHandler;
	}

	public ContainerPersistenceImpl() {
	}
	
	@InitMethod
	public void init() throws IOException, MalformedURLException, SAXException {
		Enumeration<URL> persistenceUrls = Thread.currentThread()
			.getContextClassLoader()
			.getResources("META-INF/persistence.xml");
		while (persistenceUrls.hasMoreElements()) {
			URL url = persistenceUrls.nextElement();
			URL rootUrl = getPersistenceUnitRootUrl(url);
			InputStream in = url.openStream();
			try {
				parser.parse(url.openStream(), persistenceUnitInfoHandler);
				List<PersistenceUnitInfoImpl> puiList = persistenceUnitInfoHandler.getPersistenceUnitInfoList();
				for (PersistenceUnitInfoImpl info : puiList) {
					info.setPersistenceUnitRootUrl(rootUrl);
					puiMap.put(info.getPersistenceUnitName(), info);
				}
			} finally {
				in.close();
			}			
		}
		findAllProviders();
	}
	
	public PersistenceUnitInfo getPersistenceUnitInfo(String persistenceUnitName) {
		return puiMap.get(persistenceUnitName);
	}

	public EntityManagerFactory getContainerEntityManagerFactory(
			String persistenceUnitName, Map map) {
		PersistenceUnitInfo info = puiMap.get(persistenceUnitName);
		PersistenceProvider provider = providers.get(
				info.getPersistenceProviderClassName());
		if (provider != null) {
			return provider.createContainerEntityManagerFactory(info, map);
		} else {
			for (String key : providers.keySet()) {
				provider = providers.get(key);
				EntityManagerFactory factory = provider.createContainerEntityManagerFactory(info, map);
				if (factory != null) {
					return factory;
				}
			}
		}
		return null;
	}

	private URL getPersistenceUnitRootUrl(URL url) throws MalformedURLException {
		URL rootUrl = null;
		String file = url.getFile();
		file = file.substring(0, file.length() - "/META-INF/persistence.xml".length());
		if (file.endsWith("!")) {
			file = file.substring(0, file.length() - 1);
		}
		if ("jar".equals(url.getProtocol())) {
			rootUrl = new URL(file);
		} else {
			rootUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
		}
		return rootUrl;
	}

	private void findAllProviders() {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> resources = loader
					.getResources("META-INF/services/"
							+ PersistenceProvider.class.getName());
			Set<String> names = new HashSet<String>();
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				InputStream is = url.openStream();
				try {
					names.addAll(providerNamesFromReader(new BufferedReader(
							new InputStreamReader(is))));
				} finally {
					is.close();
				}
			}
			for (String s : names) {
				Class<? extends PersistenceProvider> providerClass = (Class<? extends PersistenceProvider>) loader
						.loadClass(s);
				providers.put(s, providerClass.newInstance());
			}
		} catch (IOException e) {
			throw new PersistenceException(e);
		} catch (InstantiationException e) {
			throw new PersistenceException(e);
		} catch (IllegalAccessException e) {
			throw new PersistenceException(e);
		} catch (ClassNotFoundException e) {
			throw new PersistenceException(e);
		}
	}

	private static final Pattern nonCommentPattern = Pattern.compile("^([^#]+)");

	private static Set<String> providerNamesFromReader(BufferedReader reader)
			throws IOException {
		Set<String> names = new HashSet<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			Matcher m = nonCommentPattern.matcher(line);
			if (m.find()) {
				names.add(m.group().trim());
			}
		}
		return names;
	}

}
