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

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.spi.ClassTransformer;

import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;

/**
 * @author da-yoshi
 *
 */
public class TopLinkJpaInstrumentation extends JpaInstrumentationImpl {

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
