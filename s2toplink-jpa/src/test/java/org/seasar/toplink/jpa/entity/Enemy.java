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
package org.seasar.toplink.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Hidenoshin Yoshida
 *
 */
@Entity
@DiscriminatorValue("E")
public class Enemy extends Customer {
    
    private String dangeon;

    /**
     * @return dangeon
     */
    public String getDangeon() {
        return dangeon;
    }

    /**
     * @param dangeon 設定する dangeon
     */
    public void setDangeon(String dangeon) {
        this.dangeon = dangeon;
    }

}