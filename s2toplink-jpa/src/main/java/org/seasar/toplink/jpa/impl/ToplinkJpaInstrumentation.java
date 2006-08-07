package org.seasar.toplink.jpa.impl;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

public class ToplinkJpaInstrumentation extends JpaInstrumentationImpl {

	@Override
	public void transform(ClassLoader classLoader, ClassTransformer transformer) {
		if (transformer instanceof TopLinkWeaver) {
			TopLinkWeaver weaver = (TopLinkWeaver) transformer;
			for (Object obj : weaver.getClassDetailsMap().keySet()) {
				String className = (String) obj;
				try {
					byte[] temp = getClassBytes(classLoader, className);
					byte[] bytes = transformer.transform(classLoader, className, null, protectionDomain, temp);
					if (bytes != null) {
						defineClassMethod.invoke(
								classLoader,
								className.replace('/', '.'),
								bytes,
								0,
							    bytes.length,
							    protectionDomain);
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch (IllegalClassFormatException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			}
		}
	}


}
