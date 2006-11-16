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
//        final HibernateAttributeDesc[] attributes = getEntityDesc()
//                .getAttributeDescs();
//
//        for (int i = 0; i < attributes.length; i++) {
//            final HibernateAttributeDesc attribute = attributes[i];
//
//            if (!(attribute.isSelectable() && attribute.isReadTarget())) {
//                continue;
//            }
//
//            for (int j = 0; j < attribute.getTableNameSize(); j++) {
//                final String tableName = attribute.getTableName(j);
//                final String[] columnNames = attribute
//                        .getColumnNames(tableName);
//                final int[] sqlTypes = attribute.getSqlTypes();
//
//                assert columnNames.length == sqlTypes.length : columnNames.length
//                        + ", " + sqlTypes.length;
//
//                final DataTable table = dataSet.getTable(tableName);
//
//                for (int k = 0; k < sqlTypes.length; k++) {
//                    final String columnName = columnNames[k];
//                    if (columnName == null || table.hasColumn(columnName)) {
//                        continue;
//                    }
//                    table.addColumn(columnName, ColumnTypes
//                            .getColumnType(sqlTypes[k]));
//                }
//            }
//        }
    }
    
    protected TopLinkEntityDesc getEntityDesc() {
        return entityDesc;
    }

}
