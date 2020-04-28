package mil.nga.geopackage.extension.semantic;

import com.j256.ormlite.support.ConnectionSource;
import mil.nga.geopackage.db.GeoPackageDao;
import mil.nga.geopackage.extension.tile_matrix_set.ExtTileMatrix;

import java.sql.SQLException;

/**
 * @author jyutzler
 */
public class SemanticAnnotationsDao extends GeoPackageDao<SemanticAnnotations, Long> {

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
    public SemanticAnnotationsDao(ConnectionSource connectionSource,
                                  Class<SemanticAnnotations> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}

