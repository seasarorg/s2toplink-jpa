package org.seasar.toplink.jpa;

import javax.persistence.spi.ClassTransformer;

public interface JpaInstrumentation {

    void addTransformer(ClassTransformer classTransformer, ClassLoader classLoader);
}
