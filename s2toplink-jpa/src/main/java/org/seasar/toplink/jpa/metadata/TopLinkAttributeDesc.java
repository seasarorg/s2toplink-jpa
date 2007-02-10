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
package org.seasar.toplink.jpa.metadata;

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.TemporalType;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.descriptors.VersionLockingPolicy;
import oracle.toplink.essentials.internal.databaseaccess.DatabasePlatform;
import oracle.toplink.essentials.internal.helper.DatabaseField;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.mappings.ForeignReferenceMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.util.TemporalTypeUtil;

/**
 * TopLink Essentials用のAttributeDesc実装です。
 * @author Hidenoshin Yoshida
 */
public class TopLinkAttributeDesc implements AttributeDesc {

    /**
     * ServerSessionオブジェクト
     */
    protected ServerSession serverSession;

    /**
     * DatabaseMappingオブジェクト
     */
    protected DatabaseMapping mapping;

    protected Class<?> elementType;

    protected String name;

    protected int sqlType;

    protected TemporalType temporalType;

    protected Class<?> type;

    protected boolean association;

    protected boolean collection;

    protected boolean component;

    protected boolean id;

    protected boolean version;

    /**
     * コンストラクタ
     * @param mapping DatabaseMappingオブジェクト
     * @param serverSession ServerSessionオブジェクト
     */
    public TopLinkAttributeDesc(DatabaseMapping mapping,
            ServerSession serverSession) {
        this.mapping = mapping;
        this.serverSession = serverSession;
        this.name = mapping.getAttributeName();
        this.type = mapping.getAttributeAccessor().getAttributeClass();
        this.id = mapping.isPrimaryKeyMapping();
        this.collection = mapping.isCollectionMapping();
        this.association = mapping.isForeignReferenceMapping();
        if (collection && mapping instanceof ForeignReferenceMapping) {
            ForeignReferenceMapping fMapping = ForeignReferenceMapping.class
                    .cast(mapping);
            elementType = fMapping.getReferenceClass();
        }
        ClassDescriptor descriptor = mapping.getDescriptor();
        if (descriptor.usesVersionLocking()) {
            VersionLockingPolicy vPolicy = VersionLockingPolicy.class
                    .cast(descriptor.getOptimisticLockingPolicy());
            if (mapping.getField() != null
                    && vPolicy.getWriteLockFieldName().equals(
                            mapping.getField().getQualifiedName())) {
                version = true;
            }
        }
        if (mapping.getField() != null) {
            DatabaseField field = mapping.getField();
            DatabasePlatform platform = serverSession.getPlatform();
            sqlType = platform.getJDBCType(field);
        } else {
            sqlType = Types.OTHER;
        }
        this.component = mapping.isAggregateMapping();
        if (type == Date.class || type == Calendar.class) {
            temporalType = TemporalTypeUtil.fromSqlTypeToTemporalType(sqlType);
        }
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getElementType()
     */
    public Class<?> getElementType() {
        return elementType;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getSqlType()
     */
    public int getSqlType() {
        return sqlType;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getTemporalType()
     */
    public TemporalType getTemporalType() {
        return temporalType;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getType()
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#getValue(java.lang.Object)
     */
    public Object getValue(Object entity) {
        return mapping.getAttributeValueFromObject(entity);
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isAssociation()
     */
    public boolean isAssociation() {
        return association;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isCollection()
     */
    public boolean isCollection() {
        return collection;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isComponent()
     */
    public boolean isComponent() {
        return component;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isId()
     */
    public boolean isId() {
        return id;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#isVersion()
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.AttributeDesc#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object entity, Object value) {
        mapping.setAttributeValueInObject(entity, value);
    }

    /**
     * DatabaseMappingを返します。
     * @return DatabaseMapping
     */
    public DatabaseMapping getMapping() {
        return mapping;
    }

}
