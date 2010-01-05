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
 * TopLink Essentials用のEntityDesc実装です。
 * @author Hidenoshin Yoshida
 */
public class TopLinkEntityDesc implements EntityDesc {
    
    /**
     * ClassDescriptorオブジェクト
     */
    protected ClassDescriptor classDescriptor;
    
    /**
     * ServerSessionオブジェクト
     */
    protected ServerSession serverSession;
    
    /**
     * フィールド名をキー、TopLinkAttributeDescを値に持つMap
     */
    protected Map<String, TopLinkAttributeDesc> attributeDescMap;
    
    /**
     * このオブジェクトに関連するTopLinkAttributeDesc配列
     */
    protected TopLinkAttributeDesc[] attributeDescs;
    
    /**
     * このオブジェクトに関連するTopLinkAttributeDesc名
     */
    protected String[] attributeNames;
    
    /**
     * このオブジェクトに関連するEntityクラスのIDフィールドを表すAttributeDesc
     */
    protected TopLinkAttributeDesc idAttributeDesc;
    
    /**
     * このオブジェクトに関連するEntityクラスがマッピングされているテーブル名のList
     */
    protected List<String> tableNames;
    
    /**
     * コンストラクタ。 引数で渡されたentityClassの情報をserverSessionから取得してオブジェクトを生成します。
     * @param entityClass Entityクラスのクラスオブジェクト
     * @param serverSession ServerSession
     */
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

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDesc(java.lang.String)
     */
    public TopLinkAttributeDesc getAttributeDesc(String attributeName) {
        return attributeDescMap.get(attributeName);
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeDescs()
     */
    public TopLinkAttributeDesc[] getAttributeDescs() {
        return attributeDescs;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getAttributeNames()
     */
    public String[] getAttributeNames() {
        return attributeNames;
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityClass()
     */
    public Class<?> getEntityClass() {
        return classDescriptor.getJavaClass();
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getEntityName()
     */
    public String getEntityName() {
        return classDescriptor.getAlias();
    }

    /**
     * @see org.seasar.framework.jpa.metadata.EntityDesc#getIdAttributeDesc()
     */
    public TopLinkAttributeDesc getIdAttributeDesc() {
        return idAttributeDesc;
    }

    /**
     * テーブル名一覧のListを返します。
     * @return テーブル名一覧のList
     */
    public List<String> getTableNames() {
        return tableNames;
    }

    /**
     * ServerSessionを返します。
     * @return ServerSession
     */
    public ServerSession getServerSession() {
        return serverSession;
    }

    /**
     * ClassDescriptorがInheritancePolicyを保持していた場合trueを返します。
     * @return ClassDescriptorがInheritancePolicyを保持していた場合true
     */
    public boolean hasDiscriminatorColumn() {
        return classDescriptor.getInheritancePolicyOrNull() != null;
    }
    
    /**
     * ClassDescriptorからInheritancePolicyを取得して返します。
     * @return InheritancePolicy
     */
    public InheritancePolicy getInheritancePolicy() {
        return classDescriptor.getInheritancePolicyOrNull();
    }

}
