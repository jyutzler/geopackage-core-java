package mil.nga.geopackage.extension.semantic;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Describes a semantic annotation
 * and is used to populate the `gpkgext_semantic_annotations` table
 */
@DatabaseTable(tableName = SemanticAnnotations.TABLE_NAME, daoClass = SemanticAnnotationsDao.class)
public class SemanticAnnotations {
    public static final String TABLE_NAME = "gpkgext_semantic_annotations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_URI = "uri";

    @DatabaseField(columnName = COLUMN_ID, generatedId = true, canBeNull = false)
    private long id;

    @DatabaseField(columnName = COLUMN_TYPE, canBeNull = false)
    private String type;

    @DatabaseField(columnName = COLUMN_TITLE, canBeNull = false)
    private String title;

    @DatabaseField(columnName = COLUMN_DESCRIPTION)
    private String description;

    @DatabaseField(columnName = COLUMN_URI, canBeNull = false)
    private String uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
