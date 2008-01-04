/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

import oracle.toplink.essentials.internal.weaving.TopLinkWeaver;
import oracle.toplink.essentials.sessions.Session;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class MockTopLinkWeaver extends TopLinkWeaver {

    /**
     * @param session
     * @param classDetailsMap
     */
    @SuppressWarnings("unchecked")
    public MockTopLinkWeaver(Session session, Map classDetailsMap) {
        super(session, classDetailsMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return classfileBuffer;
    }


}
