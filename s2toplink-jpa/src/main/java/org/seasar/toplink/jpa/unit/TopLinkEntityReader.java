/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import java.util.Map;

import oracle.toplink.essentials.descriptors.ClassDescriptor;
import oracle.toplink.essentials.descriptors.InheritancePolicy;
import oracle.toplink.essentials.internal.databaseaccess.DatabasePlatform;
import oracle.toplink.essentials.internal.helper.DatabaseField;
import oracle.toplink.essentials.internal.helper.DatabaseTable;
import oracle.toplink.essentials.internal.indirection.UnitOfWorkQueryValueHolder;
import oracle.toplink.essentials.internal.sessions.AbstractRecord;
import oracle.toplink.essentials.mappings.DatabaseMapping;
import oracle.toplink.essentials.mappings.OneToOneMapping;
import oracle.toplink.essentials.threetier.ServerSession;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.BigDecimalType;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.jpa.metadata.AttributeDesc;
import org.seasar.framework.jpa.metadata.EntityDesc;
import org.seasar.framework.jpa.metadata.EntityDescFactory;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.toplink.jpa.metadata.TopLinkAttributeDesc;
import org.seasar.toplink.jpa.metadata.TopLinkEntityDesc;

/**
 * TopLink Essentials用のEntityReader実装です。
 * @author Hidenoshin Yoshida
 */
public class TopLinkEntityReader implements EntityReader {

    /**
     * DataSetオブジェクト
     */
    protected final DataSet dataSet = new DataSetImpl();
    
    private TopLinkEntityDesc entityDesc;

    /**
     * コンストラクタ
     */
    protected TopLinkEntityReader() {
    }
    
    /**
     * コンストラクタ
     * @param entity 対象Entityオブジェクト
     * @param topLinkEntityDesc entityに対応するTopLinkEntityDesc
     */
    public TopLinkEntityReader(Object entity, TopLinkEntityDesc topLinkEntityDesc) {
        this.entityDesc = topLinkEntityDesc;
        setupColumns();
        setupRow(entity);
        
    }

    /**
     * @see org.seasar.extension.dataset.DataReader#read()
     */
    public DataSet read() {
        return dataSet;
    }
    
    /**
     * dataSetのカラム定義を生成します。
     */
    protected void setupColumns() {
        for (String tableName : getEntityDesc().getTableNames()) {
            if (!dataSet.hasTable(tableName)) {
                dataSet.addTable(tableName);
            }
        }
        setupAttributeColumns();
        setupDiscriminatorColumn();
    }

    /**
     * entityDescからdataSetのカラム定義を生成します。
     */
    protected void setupAttributeColumns() {
        ServerSession serverSession = getEntityDesc().getServerSession();
        ClassDescriptor cd = serverSession.getClassDescriptor(getEntityDesc().getEntityClass());
        DatabasePlatform platform = serverSession.getPlatform();
        for (DatabaseField field : cd.getFields()) {
            DataTable table = dataSet.getTable(field.getTableName());
            int sqlType = platform.getJDBCType(field);
            String columnName = field.getName();
            if (!table.hasColumn(columnName)) {
                table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
            }
        }        
    }
    
    /**
     * entityDescからInheritancePolicyを取得し、継承関連のカラムをdataSetに定義します。
     */
    protected void setupDiscriminatorColumn() {
        if (!getEntityDesc().hasDiscriminatorColumn()) {
            return;
        }
        InheritancePolicy inheritancePolicy = getEntityDesc().getInheritancePolicy();
        DatabaseField field = inheritancePolicy.getClassIndicatorField();
        DataTable table = dataSet.getTable(field.getTableName());
        
        ServerSession serverSession = getEntityDesc().getServerSession();
        DatabasePlatform platform = serverSession.getPlatform();
        
        int sqlType = platform.getJDBCType(field);
        String columnName = field.getName();
        if (!table.hasColumn(columnName)) {
            table.addColumn(columnName, ColumnTypes.getColumnType(sqlType));
        }
    }
    
    /**
     * dataSetの行データを生成します。
     * @param entity 対象Entityオブジェクト
     */
    protected void setupRow(final Object entity) {
        Map<String, DataRow> rowMap = CollectionsUtil.newHashMap();
        for (TopLinkAttributeDesc attribute : getEntityDesc().getAttributeDescs()) { 
            DatabaseMapping mapping = attribute.getMapping();
            List<DatabaseField> fields = mapping.getFields();
            for (DatabaseField field : fields) {
                DataTable table = dataSet.getTable(field.getTableName());
                DataRow row = getRow(rowMap, table);
                Object value = mapping.getAttributeValueFromObject(entity);
                if (value != null) {
                    if (attribute.isComponent()) {
                        value = mapping.getDescriptor().getObjectBuilder().getBaseValueForField(field, entity);
                    } else if (attribute.isAssociation()) {
                        UnitOfWorkQueryValueHolder holder = UnitOfWorkQueryValueHolder.class.cast(value);
                        AbstractRecord record = holder.getRow();
                        if (record != null) {
                            value = record.get(field);
                        } else {
                            if (mapping instanceof OneToOneMapping) {
                                OneToOneMapping otoMapping = OneToOneMapping.class.cast(mapping);
                                if (otoMapping.getSourceToTargetKeyFields() != null) {
                                    DatabaseField fField = otoMapping.getSourceToTargetKeyFields().get(field);
                                    Object targetEntity = holder.getValue();
                                    EntityDesc fDesc = EntityDescFactory.getEntityDesc(targetEntity.getClass());
                                    AttributeDesc faDesc = fDesc.getAttributeDesc(fField.getName());
                                    if (faDesc == null) {
                                        faDesc = fDesc.getAttributeDesc(fField.getName().toLowerCase());
                                    }
                                    value = faDesc.getValue(targetEntity);
                                }
                            }
                        }
                    }
                    if (value instanceof Enum) {
                        DataColumn column = table.getColumn(field.getName());
                        ColumnType type = column.getColumnType();
                        if (type instanceof BigDecimalType) {
                            value = Enum.class.cast(value).ordinal();
                        }
                    }
                }
                row.setValue(field.getName(), value);
                if (attribute.isId()) {
                    setAdditionalIdRow(rowMap, field, value);
                }
            }
            
        }
        if (getEntityDesc().hasDiscriminatorColumn()) {
            InheritancePolicy inheritancePolicy = getEntityDesc().getInheritancePolicy();
            DatabaseField field = inheritancePolicy.getClassIndicatorField();
            DataTable table = dataSet.getTable(field.getTableName());
            DataRow row = getRow(rowMap, table);
            Object value = inheritancePolicy.getClassIndicatorMapping().get(getEntityDesc().getEntityClass());
            row.setValue(field.getName(), value);
        }
        for (String key : rowMap.keySet()) {
            rowMap.get(key).setState(RowStates.UNCHANGED);
        }
    }

    /**
     * tableに紐づくDataRowがrowMapに存在していれば返し、無かった場合は新規作成してrowMapにセットして返します。
     * @param rowMap テーブル名をキー、DataRowを値に持つMap
     * @param table DataTableオブジェクト
     * @return
     */
    protected DataRow getRow(Map<String, DataRow> rowMap, DataTable table) {
        DataRow row = rowMap.get(table.getTableName());
        if (row == null) {
            row = table.addRow();
            rowMap.put(table.getTableName(), row);
        }
        return row;
    }

    /**
     * 継承戦略を使った子クラスのID値をDataRowにセットします。
     * @param rowMap テーブル名をキー、DataRowを値に持つMap
     * @param field DatabaseField
     * @param value ID値
     */
    protected void setAdditionalIdRow(Map<String, DataRow> rowMap,
            DatabaseField field, Object value) {
        ServerSession serverSession = getEntityDesc().getServerSession();
        ClassDescriptor cd = serverSession.getClassDescriptor(getEntityDesc().getEntityClass());
        Map<DatabaseTable, Map<DatabaseField, DatabaseField>> idMap = cd.getAdditionalTablePrimaryKeyFields();
        if (idMap != null) {
            for (DatabaseTable dt : idMap.keySet()) {
                Map<DatabaseField, DatabaseField> fMap = idMap.get(dt);
                DatabaseField cField = fMap.get(field);
                if (cField != null) {
                    DataTable cTable = dataSet.getTable(dt.getName());
                    DataRow cRow = getRow(rowMap, cTable);
                    cRow.setValue(cField.getName(), value);                 
                }
            }
        }
    }

    
    /**
     * entityDescを返します。
     * @return entityDesc
     */
    protected TopLinkEntityDesc getEntityDesc() {
        return entityDesc;
    }

}
