package org.seasar.toplink.jpa.impl;

import java.lang.instrument.Instrumentation;

import oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer;

public class S2JavaSECMPInitializerAgent {
    
    public static void premain(String agentArgs, Instrumentation instr) throws Exception {
        if ((agentArgs != null) && agentArgs.equals("main")) {
            initializeFromMain(instr);
        } else {
            initializeFromAgent(agentArgs, instr);
        }
    }

    public static void initializeFromAgent(String agentArgs, Instrumentation instr) throws Exception {
        
        S2JavaSECMPInitializer.initializeFromAgent(agentArgs, instr);
    }

    public static void initializeFromMain(Instrumentation instr) throws Exception {
        JavaSECMPInitializer.globalInstrumentation = instr;
    }
}
