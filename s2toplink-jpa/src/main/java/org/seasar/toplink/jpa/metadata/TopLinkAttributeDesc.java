package org.seasar.toplink.jpa.metadata;

import javax.persistence.TemporalType;

import oracle.toplink.essentials.mappings.DatabaseMapping;

import org.seasar.framework.jpa.metadata.AttributeDesc;

public class TopLinkAttributeDesc implements AttributeDesc {
    
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
        this.type = mapping.getAttributeAccessor().getAttributeClass();
        this.name = mapping.getAttributeName();
        this.id = mapping.isPrimaryKeyMapping();
        this.collection = mapping.isCollectionMapping();
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
        // TODO 自動生成されたメソッド・スタブ
        return null;
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
        // TODO 自動生成されたメソッド・スタブ

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
