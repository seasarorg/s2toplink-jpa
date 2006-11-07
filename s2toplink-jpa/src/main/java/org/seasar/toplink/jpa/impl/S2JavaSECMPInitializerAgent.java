package org.seasar.toplink.jpa.impl;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;

import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

public class S2JavaSECMPInitializerAgent {
    
    public static final String DEFAULT_CONFIG_PATH = "s2toplink-jpa.dicon";
    
    public static void premain(String agentArgs, Instrumentation instr) throws Exception {
        if ((agentArgs != null) && agentArgs.equals("main")) {
            initializeFromMain(instr);
        } else {
            initializeFromAgent(agentArgs, instr);
        }
    }

    public static void initializeFromAgent(String agentArgs, Instrumentation instr) {
        JavaSECMPInitializer.globalInstrumentation = instr;

        String configPath = agentArgs != null ? agentArgs : DEFAULT_CONFIG_PATH;
        S2JavaSECMPInitializer.getJavaSECMPInitializer(configPath, new HashMap());
    }

    public static void initializeFromMain(Instrumentation instr) throws Exception {
        JavaSECMPInitializer.globalInstrumentation = instr;
    }
}
