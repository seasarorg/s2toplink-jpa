package org.seasar.toplink.jpa.metadata;

import javax.persistence.TemporalType;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.descriptors.VersionLockingPolicy;
import oracle.toplink.essentials.internal.databaseaccess.DatabasePlatform;
import oracle.toplink.essentials.internal.helper.DatabaseField;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.mappings.ForeignReferenceMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.framework.jpa.metadata.AttributeDesc;

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
    
    public TopLinkAttributeDesc(DatabaseMapping mapping, ServerSession serverSession) {
        this.mapping = mapping;
        this.serverSession = serverSession;
        this.name = mapping.getAttributeName();
        this.type = mapping.getAttributeAccessor().getAttributeClass();
        this.id = mapping.isPrimaryKeyMapping();
        this.collection = mapping.isCollectionMapping();
        this.association = mapping.isForeignReferenceMapping();
        if (collection && mapping instanceof ForeignReferenceMapping) {
            ForeignReferenceMapping fMapping = ForeignReferenceMapping.class.cast(mapping);
            elementType = fMapping.getReferenceClass();
        }
        ClassDescriptor descriptor = mapping.getDescriptor();
        if (descriptor.usesVersionLocking()) {
            VersionLockingPolicy vPolicy = VersionLockingPolicy.class.cast(descriptor.getOptimisticLockingPolicy());
            if (mapping.getField() != null && vPolicy.getWriteLockFieldName().equals(mapping.getField().getQualifiedName())) {
                version = true;
            }
        }
        if (mapping.getField() != null) {
            DatabaseField field = mapping.getField();
            DatabasePlatform platform = serverSession.getPlatform();
            sqlType = platform.getJDBCType(field);
        } else {
            // TODO
        }
        this.component = mapping.isAggregateMapping();
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
        // TODO
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

    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    public void setAssociation(boolean association) {
        this.association = association;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public void setComponent(boolean component) {
        this.component = component;
    }

    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }


    public DatabaseMapping getMapping() {
        return mapping;
    }

}
