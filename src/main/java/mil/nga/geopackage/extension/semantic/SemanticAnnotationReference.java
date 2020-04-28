package mil.nga.geopackage.extension.semantic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Identifies the business objects (database rows) that a semantic annotation
 * pertains to
 */
@DatabaseTable(tableName = SemanticAnnotationReference.TABLE_NAME, daoClass = SemanticAnnotationReferenceDao.class)
public class SemanticAnnotationReference {
    public static final String TABLE_NAME = "gpkgext_sa_reference";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_COLUMN_NAME = "key_column_name";
    public static final String COLUMN_KEY_VALUE = "key_value";
    public static final String COLUMN_SEMANTIC_ANNOTATION_ID = "sa_id";

    @DatabaseField(columnName = COLUMN_TABLE_NAME, canBeNull = false)
    private String tableName;

    @DatabaseField(columnName = COLUMN_COLUMN_NAME, canBeNull = false)
    private String columnName;

    @DatabaseField(columnName = COLUMN_KEY_VALUE, canBeNull = false)
    private long keyValue;

    @DatabaseField(columnName = COLUMN_SEMANTIC_ANNOTATION_ID, canBeNull = false)
    private long saID;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public long getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(long keyValue) {
        this.keyValue = keyValue;
    }

    public long getSaID() {
        return saID;
    }

    public void setSaID(long saID) {
        this.saID = saID;
    }
}
