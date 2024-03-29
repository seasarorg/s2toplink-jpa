/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * @author Hidenoshin Yoshida
 *
 */
@Embeddable
public class EmbeddedJoindSample implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -467314179120396228L;
    
    @Temporal(TemporalType.DATE)
    private Date date1;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date2;
    
    private BigDecimal decimal;

    
    public Date getDate1() {
        return date1;
    }

    
    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    
    public Date getDate2() {
        return date2;
    }

    
    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    
    public BigDecimal getDecimal() {
        return decimal;
    }

    
    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

}
