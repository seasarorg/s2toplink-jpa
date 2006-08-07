package org.seasar.toplink.jpa;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

public class DataSourceFactory implements ObjectFactory {

	public Object getObjectInstance(Object obj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		return new DelegateDataSource(name.toString());
	}

}
