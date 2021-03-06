package mil.nga.geopackage.db;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.DaoManager;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.srs.SpatialReferenceSystem;
import mil.nga.geopackage.core.srs.SpatialReferenceSystemDao;
import mil.nga.geopackage.io.ResourceIOUtils;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;
import mil.nga.geopackage.user.UserColumn;
import mil.nga.geopackage.user.UserTable;

/**
 * Executes database scripts to create GeoPackage tables
 * 
 * @author osbornb
 */
public class GeoPackageTableCreator {

	/**
	 * SQLite database
	 */
	private final GeoPackageCoreConnection db;

	/**
	 * Constructor
	 * 
	 * @param db
	 *            db connection
	 */
	public GeoPackageTableCreator(GeoPackageCoreConnection db) {
		this.db = db;
	}

	/**
	 * Get the database script name for the property
	 * 
	 * @param property
	 *            property name
	 * @return script name
	 * @since 3.3.0
	 */
	public static final String getScript(String property) {
		return GeoPackageProperties.getProperty(PropertyConstants.SQL,
				property);
	}

	/**
	 * Spatial Reference System script
	 * 
	 * @since 3.3.0
	 */
	public static final String SPATIAL_REFERENCE_SYSTEM = getScript(
			"spatial_reference_system");

	/**
	 * Contents script
	 * 
	 * @since 3.3.0
	 */
	public static final String CONTENTS = getScript("contents");

	/**
	 * Geometry Columns script
	 * 
	 * @since 3.3.0
	 */
	public static final String GEOMETRY_COLUMNS = getScript("geometry_columns");

	/**
	 * Tile Matrix Set script
	 * 
	 * @since 3.3.0
	 */
	public static final String TILE_MATRIX_SET = getScript("tile_matrix_set");

	/**
	 * Tile Matrix script
	 * 
	 * @since 3.3.0
	 */
	public static final String TILE_MATRIX = getScript("tile_matrix");

	/**
	 * Data Columns script
	 * 
	 * @since 3.3.0
	 */
	public static final String DATA_COLUMNS = getScript("data_columns");

	/**
	 * Data Column Constraints script
	 * 
	 * @since 3.3.0
	 */
	public static final String DATA_COLUMN_CONSTRAINTS = getScript(
			"data_column_constraints");

	/**
	 * Metadata script
	 * 
	 * @since 3.3.0
	 */
	public static final String METADATA = getScript("metadata");

	/**
	 * Metadata Reference script
	 * 
	 * @since 3.3.0
	 */
	public static final String METADATA_REFERENCE = getScript(
			"metadata_reference");

	/**
	 * Extensions script
	 * 
	 * @since 3.3.0
	 */
	public static final String EXTENSIONS = getScript("extensions");

	/**
	 * Tiled Gridded Coverage Data Coverage extension script
	 * 
	 * @since 3.3.0
	 */
	public static final String GRIDDED_COVERAGE = getScript(
			"2d_gridded_coverage");

	/**
	 * Tiled Gridded Coverage Data Tile extension script
	 * 
	 * @since 3.3.0
	 */
	public static final String GRIDDED_TILE = getScript("2d_gridded_tile");

	/**
	 * Extended Relations script
	 * 
	 * @since 3.3.0
	 */
	public static final String EXTENDED_RELATIONS = getScript(
			"extended_relations");

	/**
	 * Table Index script
	 * 
	 * @since 3.3.0
	 */
	public static final String TABLE_INDEX = getScript("table_index");

	/**
	 * Geometry Index script
	 * 
	 * @since 3.3.0
	 */
	public static final String GEOMETRY_INDEX = getScript("geometry_index");

	/**
	 * Index Geometry Index script
	 * 
	 * @since 3.3.0
	 */
	public static final String INDEX_GEOMETRY_INDEX = GeoPackageProperties
			.getProperty(PropertyConstants.SQL
					+ PropertyConstants.PROPERTY_DIVIDER + "geometry_index",
					"index");

	/**
	 * Unindex Geometry Index script
	 * 
	 * @since 3.3.0
	 */
	public static final String UNINDEX_GEOMETRY_INDEX = GeoPackageProperties
			.getProperty(PropertyConstants.SQL
					+ PropertyConstants.PROPERTY_DIVIDER + "geometry_index",
					"unindex");

	/**
	 * Feature Tile Link script
	 * 
	 * @since 3.3.0
	 */
	public static final String FEATURE_TILE_LINK = getScript(
			"feature_tile_link");

	/**
	 * Tile Scaling script
	 * 
	 * @since 3.3.0
	 */
	public static final String TILE_SCALING = getScript("tile_scaling");

	/**
	 * Contents Id script
	 * 
	 * @since 3.3.0
	 */
	public static final String CONTENTS_ID = getScript("contents_id");

	/**
	 * Create Spatial Reference System table and views
	 * 
	 * @return executed statements
	 */
	public int createSpatialReferenceSystem() {
		return execSQLScript(SPATIAL_REFERENCE_SYSTEM);
	}

	/**
	 * Create Contents table
	 * 
	 * @return executed statements
	 */
	public int createContents() {
		return execSQLScript(CONTENTS);
	}

	/**
	 * Create Geometry Columns table
	 * 
	 * @return executed statements
	 */
	public int createGeometryColumns() {
		return execSQLScript(GEOMETRY_COLUMNS);
	}

	/**
	 * Create Tile Matrix Set table
	 * 
	 * @return executed statements
	 */
	public int createTileMatrixSet() {
		return execSQLScript(TILE_MATRIX_SET);
	}

	/**
	 * Create Tile Matrix table
	 * 
	 * @return executed statements
	 */
	public int createTileMatrix() {
		return execSQLScript(TILE_MATRIX);
	}

	/**
	 * Create Data Columns table
	 * 
	 * @return executed statements
	 */
	public int createDataColumns() {
		return execSQLScript(DATA_COLUMNS);
	}

	/**
	 * Create Data Column Constraints table
	 * 
	 * @return executed statements
	 */
	public int createDataColumnConstraints() {
		return execSQLScript(DATA_COLUMN_CONSTRAINTS);
	}

	/**
	 * Create Metadata table
	 * 
	 * @return executed statements
	 */
	public int createMetadata() {
		return execSQLScript(METADATA);
	}

	/**
	 * Create Metadata Reference table
	 * 
	 * @return executed statements
	 */
	public int createMetadataReference() {
		return execSQLScript(METADATA_REFERENCE);
	}

	/**
	 * Create Extensions table
	 * 
	 * @return executed statements
	 */
	public int createExtensions() {
		return execSQLScript(EXTENSIONS);
	}

	/**
	 * Create the Tiled Gridded Coverage Data Coverage extension table
	 * 
	 * @return executed statements
	 * @since 1.2.1
	 */
	public int createGriddedCoverage() {
		return execSQLScript(GRIDDED_COVERAGE);
	}

	/**
	 * Create the Tiled Gridded Coverage Data Tile extension table
	 * 
	 * @return executed statements
	 * @since 1.2.1
	 */
	public int createGriddedTile() {
		return execSQLScript(GRIDDED_TILE);
	}

	/**
	 * Create the Extended Relations table
	 * 
	 * @return executed statements
	 * @since 3.0.1
	 */
	public int createExtendedRelations() {
		return execSQLScript(EXTENDED_RELATIONS);
	}

	/**
	 * Create Table Index table
	 * 
	 * @return executed statements
	 * @since 1.1.0
	 */
	public int createTableIndex() {
		return execSQLScript(TABLE_INDEX);
	}

	/**
	 * Create Geometry Index table
	 * 
	 * @return executed statements
	 * @since 1.1.0
	 */
	public int createGeometryIndex() {
		return execSQLScript(GEOMETRY_INDEX) + indexGeometryIndex();
	}

	/**
	 * Create Geometry Index table column indexes
	 * 
	 * @return executed statements
	 * @since 3.1.0
	 */
	public int indexGeometryIndex() {
		return execSQLScript(INDEX_GEOMETRY_INDEX);
	}

	/**
	 * Un-index (drop) Geometry Index table column indexes
	 * 
	 * @return executed statements
	 * @since 3.1.0
	 */
	public int unindexGeometryIndex() {
		return execSQLScript(UNINDEX_GEOMETRY_INDEX);
	}

	/**
	 * Create Feature Tile Link table
	 * 
	 * @return executed statements
	 * @since 1.1.5
	 */
	public int createFeatureTileLink() {
		return execSQLScript(FEATURE_TILE_LINK);
	}

	/**
	 * Create Tile Scaling table
	 * 
	 * @return executed statements
	 * @since 2.0.2
	 */
	public int createTileScaling() {
		return execSQLScript(TILE_SCALING);
	}

	/**
	 * Create Contents Id table
	 * 
	 * @return executed statements
	 * @since 3.2.0
	 */
	public int createContentsId() {
		return execSQLScript(CONTENTS_ID);
	}

	/**
	 * Execute the SQL Script
	 * 
	 * @param sqlScript
	 *            SQL script property file name
	 * @return executed statements
	 * @since 3.3.0
	 */
	public int execSQLScript(String sqlScript) {

		List<String> statements = readSQLScript(sqlScript);

		for (String statement : statements) {
			db.execSQL(statement);
		}

		return statements.size();
	}

	/**
	 * Read the SQL Script and parse the statements
	 * 
	 * @param sqlScript
	 *            SQL script property file name
	 * @return statements
	 * @since 3.3.0
	 */
	public static List<String> readSQLScript(String sqlScript) {
		String path = GeoPackageProperties.getProperty(PropertyConstants.SQL,
				"directory");
		List<String> statements = ResourceIOUtils.parseSQLStatements(path,
				sqlScript);
		return statements;
	}

	/**
	 * Create the user defined table
	 * 
	 * @param table
	 *            user table
	 * @param <TColumn>
	 *            column type
	 */
	public <TColumn extends UserColumn> void createTable(
			UserTable<TColumn> table) {

		// Verify the table does not already exist
		if (db.tableExists(table.getTableName())) {
			throw new GeoPackageException(
					"Table already exists and can not be created: "
							+ table.getTableName());
		}

		// Build the create table sql
		String sql = CoreSQLUtils.createTableSQL(table);

		// Create the table
		db.execSQL(sql);
	}

	/**
	 * Create the minimum required GeoPackage tables
	 */
	public void createRequired() {

		// Create the Spatial Reference System table (spec Requirement 10)
		createSpatialReferenceSystem();

		// Create the Contents table (spec Requirement 13)
		createContents();

		// Create the required Spatial Reference Systems (spec Requirement
		// 11)
		try {
			SpatialReferenceSystemDao dao = DaoManager.createDao(
					db.getConnectionSource(), SpatialReferenceSystem.class);
			dao.createWgs84();
			dao.createUndefinedCartesian();
			dao.createUndefinedGeographic();
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Error creating default required Spatial Reference Systems",
					e);
		}
	}

	/**
	 * Drop the table if it exists
	 * 
	 * @param table
	 *            table name
	 * @since 1.1.5
	 */
	public void dropTable(String table) {
		CoreSQLUtils.dropTable(db, table);
	}

}
