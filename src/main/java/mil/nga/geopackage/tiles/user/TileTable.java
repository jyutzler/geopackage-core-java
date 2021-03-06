package mil.nga.geopackage.tiles.user;

import java.util.ArrayList;
import java.util.List;

import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDataType;
import mil.nga.geopackage.db.GeoPackageDataType;
import mil.nga.geopackage.db.table.UniqueConstraint;
import mil.nga.geopackage.user.UserTable;

/**
 * Represents a user tile table
 * 
 * @author osbornb
 */
public class TileTable extends UserTable<TileColumn> {

	/**
	 * Id column name, Requirement 52
	 */
	public static final String COLUMN_ID = "id";

	/**
	 * Zoom level column name, Requirement 53
	 */
	public static final String COLUMN_ZOOM_LEVEL = "zoom_level";

	/**
	 * Tile column column name, Requirement 54
	 */
	public static final String COLUMN_TILE_COLUMN = "tile_column";

	/**
	 * Tile row column name, Requirement 55
	 */
	public static final String COLUMN_TILE_ROW = "tile_row";

	/**
	 * Tile ID column name, implied requirement
	 */
	public static final String COLUMN_TILE_DATA = "tile_data";

	/**
	 * Zoom level column index
	 */
	private final int zoomLevelIndex;

	/**
	 * Tile column column index
	 */
	private final int tileColumnIndex;

	/**
	 * Tile row column index
	 */
	private final int tileRowIndex;

	/**
	 * Tile data column index
	 */
	private final int tileDataIndex;

	/**
	 * Constructor
	 * 
	 * @param tableName
	 *            table name
	 * @param columns
	 *            columns
	 */
	public TileTable(String tableName, List<TileColumn> columns) {
		super(tableName, columns);

		Integer zoomLevel = null;
		Integer tileColumn = null;
		Integer tileRow = null;
		Integer tileData = null;

		// Build a unique constraint on zoom level, tile column, and tile data
		UniqueConstraint uniqueConstraint = new UniqueConstraint();

		// Find the required columns
		for (TileColumn column : columns) {

			String columnName = column.getName();
			int columnIndex = column.getIndex();

			if (columnName.equals(COLUMN_ZOOM_LEVEL)) {
				duplicateCheck(columnIndex, zoomLevel, COLUMN_ZOOM_LEVEL);
				typeCheck(GeoPackageDataType.INTEGER, column);
				zoomLevel = columnIndex;
				uniqueConstraint.add(column);
			} else if (columnName.equals(COLUMN_TILE_COLUMN)) {
				duplicateCheck(columnIndex, tileColumn, COLUMN_TILE_COLUMN);
				typeCheck(GeoPackageDataType.INTEGER, column);
				tileColumn = columnIndex;
				uniqueConstraint.add(column);
			} else if (columnName.equals(COLUMN_TILE_ROW)) {
				duplicateCheck(columnIndex, tileRow, COLUMN_TILE_ROW);
				typeCheck(GeoPackageDataType.INTEGER, column);
				tileRow = columnIndex;
				uniqueConstraint.add(column);
			} else if (columnName.equals(COLUMN_TILE_DATA)) {
				duplicateCheck(columnIndex, tileData, COLUMN_TILE_DATA);
				typeCheck(GeoPackageDataType.BLOB, column);
				tileData = columnIndex;
			}

		}

		// Verify the required columns were found
		missingCheck(zoomLevel, COLUMN_ZOOM_LEVEL);
		zoomLevelIndex = zoomLevel;

		missingCheck(tileColumn, COLUMN_TILE_COLUMN);
		tileColumnIndex = tileColumn;

		missingCheck(tileRow, COLUMN_TILE_ROW);
		tileRowIndex = tileRow;

		missingCheck(tileData, COLUMN_TILE_DATA);
		tileDataIndex = tileData;

		// Add the unique constraint
		addConstraint(uniqueConstraint);

	}

	/**
	 * Copy Constructor
	 * 
	 * @param tileTable
	 *            tile table
	 * @since 3.3.0
	 */
	public TileTable(TileTable tileTable) {
		super(tileTable);
		this.zoomLevelIndex = tileTable.zoomLevelIndex;
		this.tileColumnIndex = tileTable.tileColumnIndex;
		this.tileRowIndex = tileTable.tileRowIndex;
		this.tileDataIndex = tileTable.tileDataIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TileTable copy() {
		return new TileTable(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataType() {
		return ContentsDataType.TILES.getName();
	}

	/**
	 * Get the zoom level column index
	 * 
	 * @return zoom level index
	 */
	public int getZoomLevelColumnIndex() {
		return zoomLevelIndex;
	}

	/**
	 * Get the zoom level column
	 * 
	 * @return tile column
	 */
	public TileColumn getZoomLevelColumn() {
		return getColumn(zoomLevelIndex);
	}

	/**
	 * Get the tile column column index
	 * 
	 * @return tile column index
	 */
	public int getTileColumnColumnIndex() {
		return tileColumnIndex;
	}

	/**
	 * Get the tile column column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileColumnColumn() {
		return getColumn(tileColumnIndex);
	}

	/**
	 * Get the tile row column index
	 * 
	 * @return tile row index
	 */
	public int getTileRowColumnIndex() {
		return tileRowIndex;
	}

	/**
	 * Get the tile row column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileRowColumn() {
		return getColumn(tileRowIndex);
	}

	/**
	 * Get the tile data column index
	 * 
	 * @return tile data index
	 */
	public int getTileDataColumnIndex() {
		return tileDataIndex;
	}

	/**
	 * Get the tile data column
	 * 
	 * @return tile column
	 */
	public TileColumn getTileDataColumn() {
		return getColumn(tileDataIndex);
	}

	/**
	 * Create the required table columns
	 * 
	 * @return tile columns
	 */
	public static List<TileColumn> createRequiredColumns() {

		List<TileColumn> columns = new ArrayList<TileColumn>();
		columns.add(TileColumn.createIdColumn());
		columns.add(TileColumn.createZoomLevelColumn());
		columns.add(TileColumn.createTileColumnColumn());
		columns.add(TileColumn.createTileRowColumn());
		columns.add(TileColumn.createTileDataColumn());

		return columns;
	}

	/**
	 * Create the required table columns, starting at the provided index
	 * 
	 * @param startingIndex
	 *            starting index
	 * @return tile columns
	 */
	public static List<TileColumn> createRequiredColumns(int startingIndex) {

		List<TileColumn> columns = new ArrayList<TileColumn>();
		columns.add(TileColumn.createIdColumn(startingIndex++));
		columns.add(TileColumn.createZoomLevelColumn(startingIndex++));
		columns.add(TileColumn.createTileColumnColumn(startingIndex++));
		columns.add(TileColumn.createTileRowColumn(startingIndex++));
		columns.add(TileColumn.createTileDataColumn(startingIndex++));

		return columns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateContents(Contents contents) {
		// Verify the Contents have a tiles data type
		ContentsDataType dataType = contents.getDataType();
		if (dataType == null
				|| (dataType != ContentsDataType.TILES && dataType != ContentsDataType.GRIDDED_COVERAGE)) {
			throw new GeoPackageException("The "
					+ Contents.class.getSimpleName() + " of a "
					+ TileTable.class.getSimpleName()
					+ " must have a data type of "
					+ ContentsDataType.TILES.getName() + " or "
					+ ContentsDataType.GRIDDED_COVERAGE.getName());
		}
	}

}
