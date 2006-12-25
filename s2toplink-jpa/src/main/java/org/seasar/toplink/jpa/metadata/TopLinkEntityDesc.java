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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.descriptors.InheritancePolicy;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.framework.jpa.metadata.EntityDesc;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityDesc implements EntityDesc {
    
    protected ClassDescriptor classDescriptor;
    
    private ServerSession serverSession;
    
    protected Map<String, TopLinkAttributeDesc> attributeDescMap;
    
    protected TopLinkAttributeDesc[] attributeDescs;
    
    protected String[] attributeNames;
    
    private TopLinkAttributeDesc idAttributeDesc;
    
    private List<String> tableNames;
    
    @SuppressWarnings("unchecked")
    public TopLinkEntityDesc(Class<?> entityClass, ServerSession serverSession) {
        this.serverSession = serverSession;
        this.classDescriptor = serverSession.getClassDescriptor(entityClass);
        List<DatabaseMapping> mappings = classDescriptor.getMappings();
        int size = mappings.size();
        attributeDescs = new TopLinkAttributeDesc[size];
        attributeNames = new String[size];
        attributeDescMap = new HashMap<String, TopLinkAttributeDesc>(size);
        for (int i = 0; i < size; i++) {
            DatabaseMapping mapping = mappings.get(i);
            attributeDescs[i] = new TopLinkAttributeDesc(mapping, serverSession);
            attributeNames[i] = attributeDescs[i].getName();
            attributeDescMap.put(attributeDescs[i].getName(), attributeDescs[i]);
            if (mapping.isPrimaryKeyMapping()) {
                idAttributeDesc = attributeDescs[i];
            }
        }
        tableNames = (List<String>) classDescriptor.getTableNames();
        
    }

    public TopLinkAttributeDesc getAttributeDesc(String attributeName) {
        return attributeDescMap.get(attributeName);
    }

    public TopLinkAttributeDesc[] getAttributeDescs() {
        return attributeDescs;
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }

    public Class<?> getEntityClass() {
        return classDescriptor.getJavaClass();
    }

    public String getEntityName() {
        return classDescriptor.getAlias();
    }

    public TopLinkAttributeDesc getIdAttributeDesc() {
        return idAttributeDesc;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public ServerSession getServerSession() {
        return serverSession;
    }

    public boolean hasDiscriminatorColumn() {
        return classDescriptor.getInheritancePolicyOrNull() != null;
    }
    
    public InheritancePolicy getInheritancePolicy() {
        return classDescriptor.getInheritancePolicyOrNull();
    }

}
