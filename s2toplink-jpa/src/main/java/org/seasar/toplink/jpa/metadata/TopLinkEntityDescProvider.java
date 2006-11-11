package org.seasar.toplink.jpa.metadata;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerFactoryImpl;
import oracle.toplink.essentials.sessions.Project;

import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;


public class TopLinkEntityDescProvider implements EntityDescProvider {
    
    protected EntityManagerFactoryImpl entityManagerFactoryImpl;
    
    protected Map<Class<?>, ClassDescriptor> classDescriptorMap = CollectionsUtil.newHashMap();

    @SuppressWarnings("unchecked")
    public TopLinkEntityDescProvider(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactoryImpl = EntityManagerFactoryImpl.class.cast(entityManagerFactory);        
        Project project = entityManagerFactoryImpl.getServerSession().getProject();
        classDescriptorMap = (Map<Class<?>, ClassDescriptor>) project.getDescriptors();
    }

    public EntityDesc createEntityDesc(Class<?> entityClass) {
        ClassDescriptor classDescriptor = classDescriptorMap.get(entityClass);
        return new TopLinkEntityDesc(ClassDescriptor.class.cast(classDescriptor.clone()));
    }

}
