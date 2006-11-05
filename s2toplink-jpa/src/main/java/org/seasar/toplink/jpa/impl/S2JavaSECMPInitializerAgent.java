package org.seasar.toplink.jpa.impl;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

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
        Class cls = Class.forName("oracle.toplink.essentials.internal.ejb.cmp3.JavaSECMPInitializer");
        Field field = cls.getField("globalInstrumentation");
        field.set(null, instr);
    }
}
