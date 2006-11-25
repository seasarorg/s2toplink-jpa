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
