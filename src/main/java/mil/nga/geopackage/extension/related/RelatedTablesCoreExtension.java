package mil.nga.geopackage.extension.related;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mil.nga.geopackage.GeoPackageConstants;
import mil.nga.geopackage.GeoPackageCore;
import mil.nga.geopackage.GeoPackageException;
import mil.nga.geopackage.core.contents.Contents;
import mil.nga.geopackage.core.contents.ContentsDao;
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
	 * Get the extended relations DAO
	 * 
	 * @return extended relations DAO
	 */
	public ExtendedRelationsDao getExtendedRelationsDao() {
		return extendedRelationsDao;
	}

	/**
	 * Get or create the extension
	 * 
	 * @return extension
	 */
	private Extensions getOrCreate() {

		// Create table
		geoPackage.createExtendedRelationsTable();

		Extensions extension = getOrCreate(EXTENSION_NAME,
				ExtendedRelation.TABLE_NAME, null, EXTENSION_DEFINITION,
				ExtensionScopeType.READ_WRITE);

		return extension;
	}

	/**
	 * Get or create the extension
	 * 
	 * @param mappingTable
	 *            mapping table name
	 * @return extension
	 */
	private Extensions getOrCreate(String mappingTable) {

		getOrCreate();

		Extensions extension = getOrCreate(EXTENSION_NAME, mappingTable, null,
				EXTENSION_DEFINITION, ExtensionScopeType.READ_WRITE);

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
	 * Determine if the GeoPackage has the extension for the mapping table
	 * 
	 * @param mappingTable
	 *            mapping table name
	 * @return true if has extension
	 */
	public boolean has(String mappingTable) {
		return has() && has(EXTENSION_NAME, mappingTable, null);
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
	 * Set the contents in the user related table
	 * 
	 * @param table
	 *            user related table
	 */
	protected void setContents(UserRelatedTable table) {
		ContentsDao dao = geoPackage.getContentsDao();
		Contents contents = null;
		try {
			contents = dao.queryForId(table.getTableName());
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to retrieve "
					+ Contents.class.getSimpleName() + " for table name: "
					+ table.getTableName(), e);
		}
		if (contents == null) {
			throw new GeoPackageException(
					"No Contents Table exists for table name: "
							+ table.getTableName());
		}
		table.setContents(contents);
	}

	/**
	 * Returns the relationships defined through this extension
	 * 
	 * @return a list of ExtendedRelation objects
	 */
	public List<ExtendedRelation> getRelationships() {
		List<ExtendedRelation> result = null;
		try {
			if (extendedRelationsDao.isTableExists()) {
				result = extendedRelationsDao.queryForAll();
			} else {
				result = new ArrayList<>();
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to query for relationships "
					+ "in " + EXTENSION_NAME, e);
		}
		return result;
	}

	/**
	 * Adds a relationship between the base and related tables, creating a
	 * simple mapping table
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationType
	 *            relation type
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			RelationType relationType) {
		return addRelationship(baseTableName, relatedTableName,
				mappingTableName, relationType.getName());
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationAuthor, String relationName) {
		return addRelationship(baseTableName, relatedTableName,
				mappingTableName, getRelationName(relationAuthor, relationName));
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param mappingTableName
	 *            mapping table name
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, String mappingTableName,
			String relationName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		ExtendedRelation extendedRelation = addRelationship(baseTableName,
				relatedTableName, userMappingTable, relationName);

		return extendedRelation;
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationType
	 *            relation type
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			RelationType relationType) {
		return addRelationship(baseTableName, relatedTableName,
				userMappingTable, relationType.getName());
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			String relationAuthor, String relationName) {
		return addRelationship(baseTableName, relatedTableName,
				userMappingTable, getRelationName(relationAuthor, relationName));
	}

	/**
	 * Adds a relationship between the base and related tables
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param userMappingTable
	 *            user mapping table
	 * @param relationName
	 *            relation name
	 * @return The relationship that was added
	 */
	public ExtendedRelation addRelationship(String baseTableName,
			String relatedTableName, UserMappingTable userMappingTable,
			String relationName) {

		getOrCreate(userMappingTable.getTableName());
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
		extendedRelation.setRelationName(relationName);
		try {
			extendedRelationsDao.create(extendedRelation);
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to add relationship '"
					+ relationName + "' between " + baseTableName + " and "
					+ relatedTableName, e);
		}
		return extendedRelation;
	}

	/**
	 * Create the related table, content row, and add a relationship between the
	 * base and related table
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param mappingTableName
	 *            user mapping table name
	 * @return The relationship that was added
	 */
	public ExtendedRelation createRelationship(String baseTableName,
			UserRelatedTable relatedTable, String mappingTableName) {

		UserMappingTable userMappingTable = UserMappingTable
				.create(mappingTableName);

		return createRelationship(baseTableName, relatedTable, userMappingTable);
	}

	/**
	 * Create the related table, content row, and add a relationship between the
	 * base and related table
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTable
	 *            user related table
	 * @param userMappingTable
	 *            user mapping table
	 * @return The relationship that was added
	 */
	public ExtendedRelation createRelationship(String baseTableName,
			UserRelatedTable relatedTable, UserMappingTable userMappingTable) {

		createRelatedTable(relatedTable);

		return addRelationship(baseTableName, relatedTable.getTableName(),
				userMappingTable, relatedTable.getRelationName());
	}

	/**
	 * Create a user related table
	 * 
	 * @param relatedTable
	 *            user related table
	 */
	public void createRelatedTable(UserRelatedTable relatedTable) {

		geoPackage.createUserTable(relatedTable);

		try {
			// Create the contents
			Contents contents = new Contents();
			contents.setTableName(relatedTable.getTableName());
			contents.setDataTypeString(relatedTable.getRelationName());
			contents.setIdentifier(relatedTable.getTableName());
			ContentsDao contentsDao = geoPackage.getContentsDao();
			contentsDao.create(contents);
			contentsDao.refresh(contents);

			relatedTable.setContents(contents);

		} catch (RuntimeException e) {
			geoPackage.deleteTableQuietly(relatedTable.getTableName());
			throw e;
		} catch (SQLException e) {
			geoPackage.deleteTableQuietly(relatedTable.getTableName());
			throw new GeoPackageException(
					"Failed to create table and metadata: "
							+ relatedTable.getTableName(), e);
		}

	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationType
	 *            relation type
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, RelationType relationType) {
		removeRelationship(baseTableName, relatedTableName,
				relationType.getName());
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationAuthor
	 *            relation author
	 * @param relationName
	 *            relation name
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationAuthor, String relationName) {
		removeRelationship(baseTableName, relatedTableName,
				getRelationName(relationAuthor, relationName));
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param extendedRelation
	 *            extended relation
	 */
	public void removeRelationship(ExtendedRelation extendedRelation) {
		removeRelationship(extendedRelation.getBaseTableName(),
				extendedRelation.getRelatedTableName(),
				extendedRelation.getRelationName());
	}

	/**
	 * Remove a specific relationship from the GeoPackage
	 * 
	 * @param baseTableName
	 *            base table name
	 * @param relatedTableName
	 *            related table name
	 * @param relationName
	 *            relation name
	 */
	public void removeRelationship(String baseTableName,
			String relatedTableName, String relationName) {

		try {
			if (extendedRelationsDao.isTableExists()) {
				Map<String, Object> fieldValues = new HashMap<String, Object>();
				fieldValues.put(ExtendedRelation.COLUMN_BASE_TABLE_NAME,
						baseTableName);
				fieldValues.put(ExtendedRelation.COLUMN_RELATED_TABLE_NAME,
						relatedTableName);
				fieldValues.put(ExtendedRelation.COLUMN_RELATION_NAME,
						relationName);
				List<ExtendedRelation> extendedRelations = extendedRelationsDao
						.queryForFieldValues(fieldValues);
				for (ExtendedRelation extendedRelation : extendedRelations) {
					geoPackage.deleteTable(extendedRelation
							.getMappingTableName());
				}
				extendedRelationsDao.delete(extendedRelations);
			}
		} catch (SQLException e) {
			throw new GeoPackageException("Failed to remove relationship '"
					+ relationName + "' between " + baseTableName + " and "
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
					geoPackage.deleteTable(extendedRelation
							.getMappingTableName());
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

	/**
	 * Get the custom relation name with author
	 * 
	 * @param author
	 *            relation author
	 * @param name
	 *            base relation name
	 * @return custom relation name
	 */
	public String getRelationName(String author, String name) {
		return "x-" + author + "_" + name;
	}

}