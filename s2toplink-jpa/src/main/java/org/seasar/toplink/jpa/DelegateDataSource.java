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
package org.seasar.toplink.jpa;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author da-yoshi
 *
 */
public class DelegateDataSource implements DataSource {
	
	private String name;

	public DelegateDataSource(String name) {
		this.name = name;
	}

	public Connection getConnection() throws SQLException {
		return getParent().getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return getParent().getConnection(username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return getParent().getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return getParent().getLoginTimeout();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		getParent().setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		getParent().setLoginTimeout(seconds);

	}
	
	private DataSource getParent() {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		return (DataSource) container.getComponent(JndiResourceLocator.resolveName(name));
		
	}

}
