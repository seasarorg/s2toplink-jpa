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
package example.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 * @author Hidenoshin Yoshida
 *
 */
@Entity
public class TestB implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5294393117477590031L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    
    @Version
    private Integer version;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private TestA testa;

    /**
     * @return id を戻します。
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 設定する id。
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name を戻します。
     */
    public String getName() {
        return name;
    }

    /**
     * @param name 設定する name。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return testa を戻します。
     */
    public TestA getTesta() {
        return testa;
    }

    /**
     * @param testa 設定する testa。
     */
    public void setTesta(TestA testa) {
        this.testa = testa;
    }

    /**
     * @return version を戻します。
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version 設定する version。
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}
