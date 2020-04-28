package mil.nga.geopackage.extension.semantic;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;

import java.sql.SQLException;

/**
 * @author jyutzler
 */
public class SemanticAnnotationReferenceDao extends GeoPackageDao<SemanticAnnotationReference, Long> {

    /**
     * Constructor, required by ORMLite
     *
     * @param connectionSource
     *            connection source
     * @param dataClass
     *            data class
     * @throws SQLException
     *             upon failure
     */
    public SemanticAnnotationReferenceDao(ConnectionSource connectionSource,
                                          Class<SemanticAnnotationReference> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}

