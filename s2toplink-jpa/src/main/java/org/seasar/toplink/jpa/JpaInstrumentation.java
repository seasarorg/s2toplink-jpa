package org.seasar.toplink.jpa;

import javax.persistence.spi.ClassTransformer;

public interface JpaInstrumentation {
	void transform(ClassLoader classLoader, ClassTransformer transformer);
}
