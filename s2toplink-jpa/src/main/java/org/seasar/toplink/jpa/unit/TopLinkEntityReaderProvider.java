package org.seasar.toplink.jpa.unit;

import java.util.Collection;

import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderFactory;
import org.seasar.framework.jpa.unit.EntityReaderProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.metadata.TopLinkEntityDesc;

public class TopLinkEntityReaderProvider implements EntityReaderProvider {

    @InitMethod
    public void register() {
        EntityReaderFactory.addProvider(this);
    }

    @DestroyMethod
    public void unregister() {
        EntityReaderFactory.removeProvider(this);
    }

    public TopLinkEntityReader createEntityReader(final Object entity) {
        if (entity == null) {
            return null;
        }
        final TopLinkEntityDesc entityDesc = getEntityDesc(entity
                .getClass());
        if (entityDesc == null) {
            return null;
        }
        return new TopLinkEntityReader(entity, entityDesc);
    }

    public EntityReader createEntityReader(final Collection<?> entities) {
//        if (entities == null) {
//            return null;
//        }
//
//        final Collection<Object> newEntities = flatten(entities);
//        if (newEntities.isEmpty()) {
//            return null;
//        }
//
//        final Map<Class<?>, HibernateEntityDesc<?>> entityDescs = CollectionsUtil
//                .newHashMap();
//        for (final Object entity : newEntities) {
//            final Class<?> entityClass = entity.getClass();
//            if (entityDescs.containsKey(entityClass)) {
//                continue;
//            }
//            final HibernateEntityDesc<?> entityDesc = getEntityDesc(entityClass);
//            if (entityDescs == null) {
//                return null;
//            }
//            entityDescs.put(entityClass, entityDesc);
//        }
//        return new HibernateEntityCollectionReader(newEntities, entityDescs);
        return null;
    }

    protected Collection<Object> flatten(final Collection<?> entities) {
        Collection<Object> newEntities = CollectionsUtil.newArrayList(entities
                .size());
        for (final Object element : entities) {
            if (element instanceof Object[]) {
                for (final Object nested : Object[].class.cast(element)) {
                    newEntities.add(nested);
                }
            } else {
                newEntities.add(element);
            }
        }
        return newEntities;
    }

    protected TopLinkEntityDesc getEntityDesc(final Class<?> entityClass) {
        final EntityDesc entityDesc = EntityDescFactory
                .getEntityDesc(entityClass);
        if (entityDesc == null || !(entityDesc instanceof TopLinkEntityDesc)) {
            return null;
        }
        return TopLinkEntityDesc.class.cast(entityDesc);
    }
}
