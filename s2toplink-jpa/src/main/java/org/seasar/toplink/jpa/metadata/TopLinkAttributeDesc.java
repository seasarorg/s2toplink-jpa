package org.seasar.toplink.jpa.metadata;

import javax.persistence.TemporalType;

import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.mappings.ForeignReferenceMapping;

import org.seasar.framework.jpa.metadata.AttributeDesc;

public class TopLinkAttributeDesc implements AttributeDesc {
    
    private DatabaseMapping mapping;
    
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
    
    public TopLinkAttributeDesc(DatabaseMapping mapping) {
        this.mapping = mapping;
        this.type = mapping.getAttributeAccessor().getAttributeClass();
        this.name = mapping.getAttributeName();
        this.id = mapping.isPrimaryKeyMapping();
        this.collection = mapping.isCollectionMapping();
        if (collection && mapping instanceof ForeignReferenceMapping) {
            ForeignReferenceMapping fMapping = ForeignReferenceMapping.class.cast(mapping);
            elementType = fMapping.getReferenceClass();
        }
    }
    
    
    public Class<?> getElementType() {
        return elementType;
    }

    public String getName() {
        return name;
    }

    public int getSqlType() {
        // TODO
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
        // TODO
        return association;
    }

    public boolean isCollection() {
        return collection;
    }

    public boolean isComponent() {
        // TODO
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

}
