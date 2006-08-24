/**
 * 
 */
package org.seasar.toplink.jpa;

import oracle.toplink.essentials.ejb.cmp3.StaticWeaver;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2StaticWeaver {

    private static final String CONFIG_PATH = "javaee5.dicon";

    public static void main(String[] args) {
        String configPath = CONFIG_PATH;
        if (args.length >= 1) {
            configPath = args[0];
        }
        SingletonS2ContainerFactory.setConfigPath(configPath);
        SingletonS2ContainerFactory.init();
        try {
            StaticWeaver.main(null);
        } finally {
            SingletonS2ContainerFactory.destroy();
        }
        
    }
}
