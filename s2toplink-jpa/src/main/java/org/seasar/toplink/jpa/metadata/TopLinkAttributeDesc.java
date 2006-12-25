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
 * @author Hidenoshin Yoshida
 * 
 */
public class TopLinkAttributeDesc implements AttributeDesc {

    protected ServerSession serverSession;

    protected DatabaseMapping mapping;

    private Class<?> elementType;

    private String name;

    private int sqlType;

    private TemporalType temporalType;

    private Class<?> type;

    private boolean association;

    private boolean collection;

    private boolean component;

    private boolean id;

    private boolean version;

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

    public Class<?> getElementType() {
        return elementType;
    }

    public String getName() {
        return name;
    }

    public int getSqlType() {
        return sqlType;
    }

    public TemporalType getTemporalType() {
        return temporalType;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getValue(Object entity) {
        return mapping.getAttributeValueFromObject(entity);
    }

    public boolean isAssociation() {
        return association;
    }

    public boolean isCollection() {
        return collection;
    }

    public boolean isComponent() {
        return component;
    }

    public boolean isId() {
        return id;
    }

    public boolean isVersion() {
        return version;
    }

    public void setValue(Object entity, Object value) {
        mapping.setAttributeValueInObject(entity, value);
    }

    protected void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    protected void setAssociation(boolean association) {
        this.association = association;
    }

    protected void setCollection(boolean collection) {
        this.collection = collection;
    }

    protected void setComponent(boolean component) {
        this.component = component;
    }

    protected void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    protected void setId(boolean id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    protected void setType(Class<?> type) {
        this.type = type;
    }

    protected void setVersion(boolean version) {
        this.version = version;
    }

    public DatabaseMapping getMapping() {
        return mapping;
    }

}
