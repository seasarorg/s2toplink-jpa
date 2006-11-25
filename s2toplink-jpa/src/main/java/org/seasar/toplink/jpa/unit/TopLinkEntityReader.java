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
package org.seasar.toplink.jpa.unit;

import java.util.List;

import oracle.toplink.essentials.internal.databaseaccess.DatabasePlatform;
import oracle.toplink.essentials.internal.helper.DatabaseField;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.toplink.jpa.metadata.TopLinkAttributeDesc;
import org.seasar.toplink.jpa.metadata.TopLinkEntityDesc;

/**
 * @author Hidenoshin Yoshida
 *
 */
public class TopLinkEntityReader implements EntityReader {

    protected final DataSet dataSet = new DataSetImpl();
    
    private TopLinkEntityDesc entityDesc;

    public TopLinkEntityReader(Object entity, TopLinkEntityDesc topLinkEntityDesc) {
        this.entityDesc = topLinkEntityDesc;
        setupColumns();
        
    }

    public DataSet read() {
        return dataSet;
    }
    
    protected void setupColumns() {
        for (String tableName : entityDesc.getTableNames()) {
            if (!dataSet.hasTable(tableName)) {
                dataSet.addTable(tableName);
            }
        }
        setupAttributeColumns();
//        setupDiscriminatorColumn();
    }

    @SuppressWarnings("unchecked")
    protected void setupAttributeColumns() {
        ServerSession serverSession = entityDesc.getServerSession();
        DatabasePlatform platform = serverSession.getPlatform();
        
        for (TopLinkAttributeDesc attribute : entityDesc.getAttributeDescs()) {
            DatabaseMapping mapping = attribute.getMapping();
            List<DatabaseField> fields = mapping.getFields();
            for (DatabaseField field : fields) {
                DataTable table = dataSet.getTable(field.getTableName());
                int sqlType = platform.getJDBCType(field);
                table.addColumn(field.getQualifiedName(), ColumnTypes.getColumnType(sqlType));
            }
            
        }
    }
    
//    protected void setupDiscriminatorColumn() {
//        if (!getEntityDesc().hasDiscriminatorColumn()) {
//            return;
//        }
//        final String tableName = getEntityDesc().getPrimaryTableName();
//        final String columnName = getEntityDesc().getDiscriminatorColumnName();
//        final DataTable table = dataSet.getTable(tableName);
//        if (!table.hasColumn(columnName)) {
//            final int sqlType = getEntityDesc().getDiscriminatorSqlType();
//            table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
//        }
//    }

    
    protected TopLinkEntityDesc getEntityDesc() {
        return entityDesc;
    }

}
