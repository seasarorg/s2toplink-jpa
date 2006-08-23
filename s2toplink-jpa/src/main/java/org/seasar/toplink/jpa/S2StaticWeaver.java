/**
 * 
 */
package org.seasar.toplink.jpa;

import oracle.toplink.essentials.ejb.cmp3.StaticWeaver;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class S2StaticWeaver {

    private static final String CONFIG_PATH = "javaee5.dicon";

    public static void main(String[] args) {
        if (args.length >= 1) {
            SingletonS2ContainerFactory.setConfigPath(args[0]);
        } else {
            SingletonS2ContainerFactory.setConfigPath(CONFIG_PATH);
        }
        SingletonS2ContainerFactory.init();
        S2Container container = SingletonS2ContainerFactory.getContainer();
        try {
            StaticWeaver.main(null);
        } finally {
            container.destroy();
        }
        
    }
}
