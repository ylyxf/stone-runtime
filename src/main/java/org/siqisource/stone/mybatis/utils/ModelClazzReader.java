package org.siqisource.stone.mybatis.utils;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.annotation.Table;
import org.siqisource.stone.mybatis.dialect.Dialect;
import org.siqisource.stone.mybatis.model.Model;
import org.siqisource.stone.mybatis.model.Property;

public class ModelClazzReader {

	/**
	 * read model from class
	 *
	 * @param mapperClazz
	 *            mapperClazz
	 * @param modelClazz
	 *            modelClazz
	 * @param metaData
	 *            metaData
	 * @param isSingleKey
	 *            isSingleKey
	 * @param dialect
	 *            dialect
	 * @return Model
	 */
	public static Model readModel(Class<?> mapperClazz, Class<?> modelClazz, DatabaseMetaData metaData,
			boolean isSingleKey, Dialect dialect) {
		// initial model
		Model model = new Model();
		model.setMapperClazz(mapperClazz);
		model.setClazz(modelClazz);
		model.setName(modelClazz.getName());
		model.setSimpleName(modelClazz.getSimpleName());
		model.setSingleKey(isSingleKey);

		Table table = modelClazz.getAnnotation(Table.class);
		// schema
		if (table == null || "".equals(table.schema())) {
			model.setTableSchema(dialect.getDefualtSchema());
		} else {
			model.setTableSchema(table.schema());
		}
		// table name
		if (table == null || "".equals(table.value())) {
			model.setTableName(NameConverter.camelToUnderlineLower(modelClazz.getSimpleName()));
		} else {
			model.setTableName(table.value());
		}

		// Key Generator
		if (table != null) {
			model.setKeyGenerator(table.keyGenerator());
		} else {
			model.setKeyGenerator(KeyGenerator.no);
		}

		if (table != null) {
			model.setKeySequence(table.keySequence());
		}

		try {
			readModelFromDatabase(model, metaData);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return model;
	}

	public static Model readModelFromDatabase(Model model, DatabaseMetaData metaData) throws SQLException {
		ResultSet rs = metaData.getTables(null, model.getTableSchema(), model.getTableName(), new String[] { "TABLE" });
		if (rs.next()) {
			rs.close();
			readColumn(model, metaData);
		} else {
			rs.close();
			throw new RuntimeException("model " + model.getName() + " point to table " + model.getTableName()
					+ ",but the table is not exist in the database ");
		}
		return model;
	}

	private static void readColumn(Model model, DatabaseMetaData metaData) throws SQLException {
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		readAllClazzFields(model.getClazz(), fieldMap);

		ResultSet rs = metaData.getColumns(null, model.getTableSchema(), model.getTableName(), null);

		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			String propertyName = NameConverter.underlineToCamelFirstLower(columnName);
			if (!fieldMap.containsKey(propertyName)) {
				continue;
			}

			Property property = new Property();
			property.setColumnName(columnName);
			Field field = fieldMap.get(propertyName);
			property.setField(field);
			String name = field.getName();
			property.setName(name);

			// jdbcType
			JdbcType jdbcType = TypeUtils.jdbcType(field.getType());
			property.setJdbcType(jdbcType);

			// Integer sqlType = rs.getInt("DATA_TYPE");
			// property.setJdbcType(JdbcType.forCode(sqlType));

			// add to model
			model.addProperty(property);
		}
		rs.close();

		// update singleKey
		if (model.isSingleKey()) {
			String primaryColumn = readSinglePrimaryColumn(model.getName(), model.getTableSchema(),
					model.getTableName(), metaData);
			String propertyName = NameConverter.underlineToCamelFirstLower(primaryColumn);
			if (fieldMap.containsKey(propertyName)) {
				model.setKeyColumn(primaryColumn);
				model.setKeyProperty(propertyName);
			} else {
				throw new RuntimeException("  model " + model.getName() + " does not have a property point to table "
						+ model.getTableSchema() + " ->" + model.getTableName() + "'s singleKey " + primaryColumn);
			}

		}
	}

	/**
	 * use the child when same name property in superclass
	 *
	 * @param clazz
	 * @param fieldMap
	 */
	private static void readAllClazzFields(Class<?> clazz, Map<String, Field> fieldMap) {
		if (clazz.getName().equals(Object.class.getName())) {
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field filed : fields) {
			if (!fieldMap.containsKey(filed.getName())) {
				fieldMap.put(filed.getName(), filed);
			}
		}
		readAllClazzFields(clazz.getSuperclass(), fieldMap);
	}

	private static String readSinglePrimaryColumn(String modelName, String schema, String table,
			DatabaseMetaData metaData) throws SQLException {
		ResultSet primaryRs = metaData.getPrimaryKeys(null, schema, table);
		String keyColumn = null;
		try {
			if (primaryRs.next()) {
				keyColumn = primaryRs.getString("COLUMN_NAME");
			} else {
				throw new RuntimeException(modelName + " mapper to an singleKey table:" + table
						+ ",but in database it has no primary key");
			}

			if (primaryRs.next()) {
				throw new RuntimeException(modelName + " mapper to an singleKey table:" + table
						+ ",but in database it has more than one column  primary keys");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			primaryRs.close();
		}
		return keyColumn;
	}

}
