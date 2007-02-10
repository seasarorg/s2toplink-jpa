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
 * javaagentにより実行され、Instrumentationオブジェクトを取得し、JavaSECMPInitializerオブジェクトの生成処理を行います。
 * @author Hidenoshin Yoshida
 */
public class S2JavaSECMPInitializerAgent {
    
    /**
     * JavaSECMPInitializerを生成する定義を記述したdiconファイルのデフォルト名
     */
    public static final String DEFAULT_CONFIG_PATH = "s2toplink-jpa.dicon";
    
    /**
     * javaagentにより実行され、InstrumentationオブジェクトをJavaSECMPInitializerのstaticフィールドに設定します。
     * @param agentArgs javaagent実行時に指定された引数
     * @param instr Instrumentationオブジェクト
     */
    public static void premain(String agentArgs, Instrumentation instr) {
        if ((agentArgs != null) && agentArgs.equals("main")) {
            initializeFromMain(instr);
        } else {
            initializeFromAgent(agentArgs, instr);
        }
    }

    /**
     * 指定されたInstrumentationオブジェクトをJavaSECMPInitializerのstaticフィールドに保持し、JavaSECMPInitializerオブジェクトを生成します。
     * @param agentArgs javaagent実行時に指定された引数
     * @param instr Instrumentationオブジェクト
     */
    @SuppressWarnings("unchecked")
    public static void initializeFromAgent(String agentArgs, Instrumentation instr) {
        JavaSECMPInitializer.globalInstrumentation = instr;

        String configPath = agentArgs != null ? agentArgs : DEFAULT_CONFIG_PATH;
        S2JavaSECMPInitializer.getJavaSECMPInitializer(configPath, new HashMap());
    }

    /**
     * 指定されたInstrumentationオブジェクトをJavaSECMPInitializerのstaticフィールドに保持します。
     * @param instr Instrumentationオブジェクト
     */
    public static void initializeFromMain(Instrumentation instr) {
        JavaSECMPInitializer.globalInstrumentation = instr;
    }
}
