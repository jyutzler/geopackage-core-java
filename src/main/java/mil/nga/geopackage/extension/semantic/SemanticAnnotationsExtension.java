package mil.nga.geopackage.extension.semantic;

import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.extension.portrayal.*;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnnotationsExtension extends BaseExtension {
    public static final String EXTENSION_AUTHOR = "im";
    public static final String EXTENSION_NAME_NO_AUTHOR = "semantic_annotations";
    public static final String EXTENSION_NAME = Extensions.buildExtensionName(EXTENSION_AUTHOR,
            EXTENSION_NAME_NO_AUTHOR);
    public static final String EXTENSION_DEFINITION = GeoPackageProperties
            .getProperty(PropertyConstants.EXTENSIONS,
                    EXTENSION_NAME_NO_AUTHOR);

    private final SemanticAnnotationsDao semanticAnnotationsDao;
    private final SemanticAnnotationReferenceDao saReferenceDao;

    /**
     * Constructor
     *
     * @param geoPackage GeoPackage
     */
    public SemanticAnnotationsExtension(GeoPackageCore geoPackage) {

        super(geoPackage);
        semanticAnnotationsDao = geoPackage.getSemanticAnnotationsDao();
        saReferenceDao = geoPackage.getSemanticAnnotationReferenceDao();
    }

    public boolean has() {

        return this.has(EXTENSION_NAME, SemanticAnnotations.TABLE_NAME, null) &&
                this.has(EXTENSION_NAME, SemanticAnnotationReference.TABLE_NAME, null) &&
                geoPackage.isTable(SemanticAnnotations.TABLE_NAME) &&
                geoPackage.isTable(SemanticAnnotationReference.TABLE_NAME);
    }

    /**
     * Get or create the extension
     *
     * @return extensions
     */
    public List<Extensions> getOrCreate() {

        geoPackage.createSemanticAnnotationsTables();

        List<Extensions> extensions = new ArrayList<>();

        extensions.add(getOrCreate(EXTENSION_NAME,
                SemanticAnnotations.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));
        extensions.add(getOrCreate(EXTENSION_NAME,
                SemanticAnnotationReference.TABLE_NAME, null, EXTENSION_DEFINITION,
                ExtensionScopeType.READ_WRITE));

        return extensions;
    }

    public void removeExtension() {
        try {
            if (this.saReferenceDao.isTableExists()) {
                this.geoPackage.dropTable(Styles.TABLE_NAME);
            }
            if (this.semanticAnnotationsDao.isTableExists()) {
                this.geoPackage.dropTable(Stylesheets.TABLE_NAME);
            }
            this.extensionsDao.deleteByExtension(EXTENSION_NAME);
        } catch (SQLException var4) {
            throw new GeoPackageException("Failed to delete Semantic Annotations extension and table. GeoPackage: " + this.geoPackage.getName(), var4);
        }
    }
}
