/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.lang.instrument.Instrumentation;
import java.util.HashMap;

import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2JavaSECMPInitializerAgent {
    
    public static final String DEFAULT_CONFIG_PATH = "s2toplink-jpa.dicon";
    
    public static void premain(String agentArgs, Instrumentation instr) throws Exception {
        if ((agentArgs != null) && agentArgs.equals("main")) {
            initializeFromMain(instr);
        } else {
            initializeFromAgent(agentArgs, instr);
        }
    }

    @SuppressWarnings("unchecked")
    public static void initializeFromAgent(String agentArgs, Instrumentation instr) {
        JavaSECMPInitializer.globalInstrumentation = instr;

        String configPath = agentArgs != null ? agentArgs : DEFAULT_CONFIG_PATH;
        S2JavaSECMPInitializer.getJavaSECMPInitializer(configPath, new HashMap());
    }

    public static void initializeFromMain(Instrumentation instr) throws Exception {
        JavaSECMPInitializer.globalInstrumentation = instr;
    }
}
