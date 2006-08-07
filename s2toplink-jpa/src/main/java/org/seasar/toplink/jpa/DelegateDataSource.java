package org.seasar.toplink.jpa;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

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
		return (DataSource) container.getComponent(name);
		
	}

}
