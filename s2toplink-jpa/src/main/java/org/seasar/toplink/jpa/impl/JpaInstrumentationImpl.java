package org.seasar.toplink.jpa.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

import javax.persistence.spi.ClassTransformer;

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.toplink.jpa.JpaInstrumentation;

public class JpaInstrumentationImpl implements JpaInstrumentation {

	// constants
    public static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    // static fields
    protected static final ProtectionDomain protectionDomain;

    protected static Method defineClassMethod;

    // static initializer
    static {
    	
        protectionDomain = AccessController
                .doPrivileged(new PrivilegedAction<ProtectionDomain>() {
                    public ProtectionDomain run() {
                        return JpaInstrumentationImpl.class.getProtectionDomain();
                    }
                });

        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                final Class[] paramTypes = new Class[] { String.class,
                        byte[].class, int.class, int.class,
                        ProtectionDomain.class };
                try {
                    final Class loader = ClassUtil.forName(ClassLoader.class
                            .getName());
                    defineClassMethod = loader.getDeclaredMethod(
                            DEFINE_CLASS_METHOD_NAME, paramTypes);
                    defineClassMethod.setAccessible(true);
                } catch (final NoSuchMethodException e) {
                    throw new NoSuchMethodRuntimeException(ClassLoader.class,
                            DEFINE_CLASS_METHOD_NAME, paramTypes, e);
                }
                return null;
            }
        });
    }

	public void transform(ClassLoader classLoader, ClassTransformer transformer) {
	}
	
	protected byte[] getClassBytes(ClassLoader classLoader, String name) throws IOException {
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new BufferedInputStream(
				classLoader.getResourceAsStream(name.replace('.', '/') + ".class"));
			out = new ByteArrayOutputStream();
			int i;
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			return out.toByteArray();
		} finally {
			if (in != null) {
		    	try {
					in.close();
				} catch (IOException e2) {
				}
			}
			if (out != null) {
		    	try {
					out.close();
				} catch (IOException e2) {
				}
			}
		}
	}

}
