package org.seasar.toplink.jpa.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.framework.jpa.metadata.EntityDesc;

public class TopLinkEntityDesc implements EntityDesc {
    
    protected ClassDescriptor classDescriptor;
    
    private ServerSession serverSession;
    
    protected Map<String, TopLinkAttributeDesc> attributeDescMap;
    
    protected TopLinkAttributeDesc[] attributeDescs;
    
    protected String[] attributeNames;
    
    private TopLinkAttributeDesc idAttributeDesc;
    
    private List<String> tableNames;
    
    @SuppressWarnings("unchecked")
    public TopLinkEntityDesc(Class<?> entityClass, EntityManagerFactoryImpl entityManagerFactoryImpl) {
        ServerSession serverSession = entityManagerFactoryImpl.getServerSession();
        this.classDescriptor = serverSession.getClassDescriptor(entityClass);
        List<DatabaseMapping> mappings = (List<DatabaseMapping>) classDescriptor.getMappings();
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

    
}
