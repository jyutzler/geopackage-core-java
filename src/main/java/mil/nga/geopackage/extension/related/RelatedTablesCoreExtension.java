package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.extension.BaseExtension;
import mil.nga.geopackage.extension.ExtensionScopeType;
import mil.nga.geopackage.extension.Extensions;
import mil.nga.geopackage.property.GeoPackageProperties;
import mil.nga.geopackage.property.PropertyConstants;

import com.j256.ormlite.table.TableUtils;

/**
 * Related Tables core extension
 * 
 * @author jyutzler
 * @since 3.0.1
 */
public abstract class RelatedTablesCoreExtension extends BaseExtension {

	/**
	 * Extension author
	 */
	public static final String EXTENSION_AUTHOR = GeoPackageConstants.GEO_PACKAGE_EXTENSION_AUTHOR;

	/**
	 * Extension name without the author
	 */
	public static final String EXTENSION_NAME_NO_AUTHOR = "related_tables";

	/**
	 * Extension, with author and name
	 * 
	 * TODO Remove the commented sections when extension is adopted
	 */
	public static final String EXTENSION_NAME = /*
												 * Extensions.buildExtensionName(
												 * EXTENSION_AUTHOR,
												 */EXTENSION_NAME_NO_AUTHOR/* ) */;

	/**
	 * Extension definition URL
	 */
	public static final String EXTENSION_DEFINITION = GeoPackageProperties
			.getProperty(PropertyConstants.EXTENSIONS, EXTENSION_NAME_NO_AUTHOR);

	/**
	 * Extended Relations DAO
	 */
	private final ExtendedRelationsDao extendedRelationsDao;

	/**
	 * Constructor
	 * 
	 * @param geoPackage
	 *            GeoPackage
	 * 
	 */
	protected RelatedTablesCoreExtension(GeoPackageCore geoPackage) {
		super(geoPackage);
		extendedRelationsDao = geoPackage.getExtendedRelationsDao();
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 */
	public Extensions getOrCreate() {

		// Create table
		geoPackage.createExtendedRelationsTable();

		Extensions extension = getOrCreate(EXTENSION_NAME,
				ExtendedRelation.TABLE_NAME, null, EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Determine if the GeoPackage has the extension
	 * 
	 * @return true if has extension
	 */
	public boolean has() {
		return has(EXTENSION_NAME, ExtendedRelation.TABLE_NAME, null);
	}

	/**
	 * Get the primary key of a table
	 * 
	 * @param tableName
	 *            table name
	 * @return the column name
	 */
	public abstract String getPrimaryKeyColumnName(String tableName);

	/**
	 * Drop the mapping table of the extended relation
	 * 
	 * @param extendedRelation
	 *            extended relation
	 */
	public abstract void dropMappingTable(ExtendedRelation extendedRelation);

	/**
	 * Returns the relationships defined through this extension
	 * 
	 * @return a collection of ExtendedRelation objects
	 */
	public Collection<ExtendedRelation> getRelationships() {
		Collection<ExtendedRelation> result = null;
		try {
			result = extendedRelationsDao.queryForAll();
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for relationships "
					+ "in " + EXTENSION_NAME, e);
		}
		return result;
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 * @param relatedTableName
	 * @param mappingTableName
	 * @param relationshipName
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationshipName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		ExtendedRelation extendedRelation = addRelationship(baseTableName,
				relatedTableName, userMappingTable, relationshipName);

		return extendedRelation;
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 * @param relatedTableName
	 * @param userMappingTable
	 * @param relationshipName
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			String relationshipName) {

		geoPackage.createUserTable(userMappingTable);

		// Add a row to gpkgext_relations
		ExtendedRelation extendedRelation = new ExtendedRelation();
		extendedRelation.setBaseTableName(baseTableName);
		extendedRelation
				.setBasePrimaryColumn(getPrimaryKeyColumnName(baseTableName));
		extendedRelation.setRelatedTableName(relatedTableName);
		extendedRelation
				.setRelatedPrimaryColumn(getPrimaryKeyColumnName(relatedTableName));
		extendedRelation.setMappingTableName(userMappingTable.getTableName());
		extendedRelation.setRelationName(relationshipName);
		try {
			extendedRelationsDao.create(extendedRelation);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to add relationship '"
					+ relationshipName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}
		return extendedRelation;
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 * @param relatedTableName
	 * @param relationshipName
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationshipName) {
		Map<String, Object> fieldValues = new HashMap<String, Object>();
		fieldValues.put(ExtendedRelation.COLUMN_BASE_TABLE_NAME, baseTableName);
		fieldValues.put(ExtendedRelation.COLUMN_RELATED_TABLE_NAME,
				relatedTableName);
		fieldValues
				.put(ExtendedRelation.COLUMN_RELATION_NAME, relationshipName);
		try {
			List<ExtendedRelation> extendedRelations = extendedRelationsDao
					.queryForFieldValues(fieldValues);
			for (ExtendedRelation extendedRelation : extendedRelations) {
				dropMappingTable(extendedRelation);
			}
			extendedRelationsDao.delete(extendedRelations);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ relationshipName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}

	}

	/**
	 * Remove all trace of the extension
	 */
	public void removeExtension() {
		ExtendedRelationsDao extendedRelationsDao = geoPackage
				.getExtendedRelationsDao();
		try {
			if (extendedRelationsDao.isTableExists()) {
				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.queryForAll();
				for (ExtendedRelation extendedRelation : extendedRelations) {
					dropMappingTable(extendedRelation);
				}
				TableUtils.dropTable(extendedRelationsDao, false);
			}
			if (extensionsDao.isTableExists()) {
				extensionsDao.deleteByExtension(EXTENSION_NAME);
			}
		} catch (SQLException e) {
			throw new GeoPackageException(
					"Failed to delete Related Tables extension and table. GeoPackage: "
							+ geoPackage.getName(), e);
		}
	}

}
