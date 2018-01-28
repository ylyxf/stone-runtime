package org.siqisource.stone.mybatis.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.dialect.Dialect;
import org.siqisource.stone.mybatis.dialect.OracleDialect;
import org.siqisource.stone.mybatis.model.Model;
import org.siqisource.stone.mybatis.model.Property;

public class XMLBuilder {

	private Model model;

	private Dialect dialect;

	private static String KEY_READ_BY = "readBy";
	private static String KEY_COUNT_BY = "countBy";
	private static String KEY_LIST_BY = "listBy";
	private static String KEY_DELETE_BY = "deleteBy";
	private static String KEY_UPDATE_BY = "updateBy";
	private static String LINKER = "And";

	public XMLBuilder(Model model, Dialect dialect) {
		this.model = model;
		this.dialect = dialect;
	}

	public InputStream genXml() {
		java.lang.StringBuffer result = new java.lang.StringBuffer();
		result.append(head());
		result.append(insert());
		if (this.model.isSingleKey()) {
			result.append(read());
			result.append(delete());
			result.append(update());
			result.append(updatePartitive());
		}

		result.append(insertPartitive());
		result.append(count());
		result.append(list());

		result.append(updateBatch());

		result.append(deleteBatch());

		Class<?> mapperClazz = this.model.getMapperClazz();
		Method[] methods = mapperClazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Select.class) || method.isAnnotationPresent(SelectProvider.class)
					|| method.isAnnotationPresent(Insert.class) || method.isAnnotationPresent(InsertProvider.class)
					|| method.isAnnotationPresent(Update.class) || method.isAnnotationPresent(UpdateProvider.class)
					|| method.isAnnotationPresent(Delete.class) || method.isAnnotationPresent(DeleteProvider.class)) {
				continue;

			}
			String name = method.getName();
			if (name.startsWith(KEY_READ_BY)) {
				result.append(readBy(method));
			} else if (name.startsWith(KEY_COUNT_BY)) {
				result.append(countBy(method));
			} else if (name.startsWith(KEY_LIST_BY)) {
				result.append(listBy(method));
			} else if (name.startsWith(KEY_DELETE_BY)) {
				result.append(deleteBy(method));
			} else if (name.startsWith(KEY_UPDATE_BY)) {
				result.append(updateBy(method));
			}

		}

		result.append(foot());
		//System.out.println(result.toString());
		return new ByteArrayInputStream(result.toString().getBytes());
	}

	public String head() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<?xml version='1.0' encoding='UTF-8'?>");
		sbString.append("<!DOCTYPE mapper PUBLIC '-//mybatis.org//DTD Mapper 3.0//EN' ");
		sbString.append(" 'http://mybatis.org/dtd/mybatis-3-mapper.dtd'>");
		sbString.append(" <mapper namespace='" + model.getNamespace() + "'>");
		return sbString.toString();
	}

	public String insert() {

		StringBuffer sbString = new StringBuffer();
		sbString.append("<insert id='insert' parameterType='" + model.getName() + "' ");

		// useGeneratedKeys
		if (model.isSingleKey() && model.getKeyGenerator() == KeyGenerator.auto_increment) {
			sbString.append("useGeneratedKeys='true' ");
			sbString.append(" keyProperty='" + model.getKeyProperty() + "'");
		}

		sbString.append(">");

		// selectKey
		KeyGenerator keyGenerator = model.getKeyGenerator();
		if (model.isSingleKey() && (keyGenerator == KeyGenerator.sequence || keyGenerator == KeyGenerator.uuid)) {
			sbString.append("<selectKey keyProperty='");
			sbString.append(model.getKeyProperty());
			sbString.append("' keyColumn='");
			sbString.append(model.getKeyColumn());
			sbString.append("' ");
			if (keyGenerator == KeyGenerator.sequence) {
				sbString.append(" resultType='integer' ");
			} else if (keyGenerator == KeyGenerator.uuid && !(dialect instanceof OracleDialect)) {
				sbString.append(" resultType='" + UUID.class.getName() + "' ");
			} else {
				sbString.append(" resultType='string' ");
			}
			sbString.append(" order='" + dialect.getSelectKeyOrder() + "'>");
			sbString.append(dialect.getKeySelector(keyGenerator, model));
			sbString.append("</selectKey>");
		}

		sbString.append(" INSERT INTO " + model.getFullTableName() + " (");

		List<Property> properties = null;
		if (model.isSingleKey() && keyGenerator == KeyGenerator.auto_increment) {
			properties = model.getNoPrimaryProperties();
		} else {
			properties = model.getProperties();
		}

		for (int i = 0, iSize = properties.size(); i < iSize; i++) {
			Property property = properties.get(i);
			sbString.append(property.getColumnName());
			sbString.append(",");
		}
		sbString.deleteCharAt(sbString.length() - 1);
		sbString.append(") VALUES ( ");
		for (int i = 0, iSize = properties.size(); i < iSize; i++) {
			Property property = properties.get(i);
			sbString.append("#{" + property.getName() + ",jdbcType=" + property.getJdbcType() + "}");
			sbString.append(",");
		}
		sbString.deleteCharAt(sbString.length() - 1);
		sbString.append(" )");
		sbString.append("</insert>");
		return sbString.toString();
	}

	public String insertPartitive() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<insert id='insertPartitive' parameterType='map'>");
		sbString.append(" INSERT INTO " + model.getFullTableName() + " (${fields._insertFields}) ");
		sbString.append(" VALUES (${fields._insertValues}) ");
		sbString.append("</insert>");
		return sbString.toString();
	}

	public String read() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<select id='read' parameterType='map' resultType='" + model.getName() + "'> ");
		sbString.append(" SELECT * FROM " + model.getFullTableName());
		sbString.append(" WHERE ");
		sbString.append(whereId());
		sbString.append("</select>");
		return sbString.toString();
	}

	public String count() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<select id='count' parameterType='map' resultType='int'>");
		sbString.append(" SELECT count(1) FROM " + model.getFullTableName());
		sbString.append(" <if test='condition.expressions != null '> ");
		sbString.append(" <where>${condition.comboedExpressions}</where> ");
		sbString.append(" </if> ");
		sbString.append("</select>");
		return sbString.toString();
	}

	public String list() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<select id='list' parameterType='map' resultType='" + model.getName() + "'>");
		sbString.append(" SELECT * FROM " + model.getFullTableName());
		sbString.append(" <if test='condition.expressions != null '> ");
		sbString.append(" <where>${condition.comboedExpressions}</where> ");
		sbString.append(" </if> ");
		sbString.append(" <if test='condition.orderBy != null '> ");
		sbString.append(" ${condition.orderBy} ");
		sbString.append(" </if> ");
		sbString.append("</select>");
		return sbString.toString();
	}

	public String update() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<update id='update' parameterType='" + model.getName() + "'>");
		sbString.append(" UPDATE " + model.getFullTableName() + " SET ");

		List<Property> noPrimaryProperties = model.getNoPrimaryProperties();
		for (Property property : noPrimaryProperties) {
			sbString.append(property.getColumnName() + "= #{" + property.getName() + ", jdbcType="
					+ property.getJdbcType() + " } ,");
		}
		// delete ',' in the last '#{ xxx ,jdbcType=yyy} ,'
		sbString.deleteCharAt(sbString.length() - 1);
		sbString.append(" WHERE ");
		sbString.append(whereId());
		sbString.append("</update>");
		return sbString.toString();
	}

	public String updatePartitive() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<update id='updatePartitive' parameterType='map' >");
		sbString.append(" UPDATE " + model.getFullTableName() + " SET ${fields._updateSql} ");
		sbString.append(" WHERE ");
		sbString.append(whereId());
		sbString.append("</update>");
		return sbString.toString();
	}

	public String updateBatch() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<update id='updateBatch' parameterType='map' >");
		sbString.append(" UPDATE " + model.getFullTableName() + " SET ${fields._updateSql} ");
		sbString.append(" <if test='condition.expressions != null'> ");
		sbString.append(" <where> ${condition.comboedExpressions} </where> ");
		sbString.append(" </if> ");
		sbString.append(" <if test='condition.expressions == null '> ");
		sbString.append(" <where>1=0</where> ");
		sbString.append(" </if> ");
		sbString.append("</update>");
		return sbString.toString();
	}

	public String delete() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<delete id='delete' parameterType='map' >");
		sbString.append(" DELETE FROM  " + model.getFullTableName() + " WHERE  ");
		sbString.append(whereId());
		sbString.append("</delete>");
		return sbString.toString();
	}

	public String deleteBatch() {
		StringBuffer sbString = new StringBuffer();
		sbString.append("<delete id='deleteBatch' parameterType='map' >");
		sbString.append(" DELETE FROM  " + model.getFullTableName());
		sbString.append(" <if test='condition.expressions != null '> ");
		sbString.append(" <where>${condition.comboedExpressions}</where> ");
		sbString.append(" </if> ");
		sbString.append(" <if test='condition.expressions == null '> ");
		sbString.append(" <where>1=0</where> ");
		sbString.append(" </if> ");
		sbString.append("</delete>");
		return sbString.toString();
	}

	private String whereId() {
		StringBuffer sbString = new StringBuffer();
		Property property = model.getPrimaryProperty();
		sbString.append(property.getColumnName() + "=#{id}");
		String result = sbString.toString();
		return result;
	}

	public String foot() {
		return "</mapper>";
	}

	/**
	 */
	private String readBy(Method method) {
		String name = method.getName();
		name = name.substring(KEY_READ_BY.length(), name.length());
		String[] propertyArray = name.split(LINKER);
		for (int i = 0; i < propertyArray.length; i++) {
			propertyArray[i] = NameConverter.firstLetterLower(propertyArray[i]);
		}
		List<String> propertyNames = Arrays.asList(propertyArray);
		List<Property> byNames = this.model.getPropertiesByNames(propertyNames);
		if (byNames.size() == 0) {
			return "";
		}

		StringBuffer sbSqlXml = new StringBuffer(256);
		sbSqlXml.append(
				"<select id='" + method.getName() + "' parameterType='map' resultType='" + model.getName() + "'> ");

		StringBuffer sbSql = new StringBuffer(256);
		sbSql.append("select * from " + model.getFullTableName());
		sbSql.append(" WHERE ");
		for (int i = 0, iSize = byNames.size(); i < iSize; i++) {
			Property property = byNames.get(i);
			sbSql.append(property.getColumnName() + " = #{param" + (i + 1) + "}");
			if (i + 1 < iSize) {
				sbSql.append(" AND ");
			}
		}
		String sql = this.dialect.getLimitString(sbSql.toString(), 0, 1);

		sbSqlXml.append(sql);
		sbSqlXml.append("</select>");
		return sbSqlXml.toString();
	}

	/**
	 * countBy
	 */
	private String countBy(Method method) {
		String name = method.getName();
		name = name.substring(KEY_COUNT_BY.length(), name.length());
		List<String> propertyNames = new ArrayList<String>();
		for (String propertyNameInMethod : name.split(LINKER)) {
			propertyNames.add(NameConverter.firstLetterLower(propertyNameInMethod));
		}
		List<Property> byNames = this.model.getPropertiesByNames(propertyNames);
		if (byNames.size() == 0) {
			return "";
		}

		StringBuffer sbSql = new StringBuffer(256);
		sbSql.append(
				"<select id='" + method.getName() + "' parameterType='map' resultType='" + model.getName() + "'> ");
		sbSql.append("select count(1) as cnt from " + model.getFullTableName());
		sbSql.append(" WHERE ");
		for (int i = 0, iSize = byNames.size(); i < iSize; i++) {
			Property property = byNames.get(i);
			sbSql.append(property.getColumnName() + " = #{param" + (i + 1) + "}");
			if (i + 1 < iSize) {
				sbSql.append(" AND ");
			}
		}
		sbSql.append("</select>");
		return sbSql.toString();
	}

	/**
	 * listBy
	 */
	private String listBy(Method method) {
		String name = method.getName();
		name = name.substring(KEY_LIST_BY.length(), name.length());
		List<String> propertyInMethodNames = Arrays.asList(name.split(LINKER));
		List<String> propertyNames = new ArrayList<String>();
		for (String propertyInMethod : propertyInMethodNames) {
			propertyNames.add(NameConverter.firstLetterLower(propertyInMethod));
		}
		List<Property> byNames = this.model.getPropertiesByNames(propertyNames);
		if (byNames.size() == 0) {
			return "";
		}

		StringBuffer sbSql = new StringBuffer(256);
		sbSql.append(
				"<select id='" + method.getName() + "' parameterType='map' resultType='" + model.getName() + "'> ");
		sbSql.append("select * from " + model.getFullTableName());
		sbSql.append(" WHERE ");
		for (int i = 0, iSize = byNames.size(); i < iSize; i++) {
			Property property = byNames.get(i);
			sbSql.append(property.getColumnName() + " = #{param" + (i + 1) + "}");
			if (i + 1 < iSize) {
				sbSql.append(" AND ");
			}
		}
		sbSql.append("</select>");
		return sbSql.toString();
	}

	/**
	 * deleteBy
	 */
	private String deleteBy(Method method) {
		String name = method.getName();
		name = name.substring(KEY_DELETE_BY.length(), name.length());
		List<String> propertyNames = Arrays.asList(name.split(LINKER));
		List<Property> byNames = this.model.getPropertiesByNames(propertyNames);
		if (byNames.size() == 0) {
			return "";
		}

		StringBuffer sbSql = new StringBuffer(256);
		sbSql.append("<delete id='" + method.getName() + "' parameterType='map' > ");
		sbSql.append("delete from " + model.getFullTableName());
		sbSql.append(" WHERE ");
		for (int i = 0, iSize = byNames.size(); i < iSize; i++) {
			Property property = byNames.get(i);
			sbSql.append(property.getColumnName() + " = #{param" + (i + 1) + "}");
			if (i + 1 < iSize) {
				sbSql.append(" AND ");
			}
		}
		sbSql.append("</delete>");
		return sbSql.toString();
	}

	/**
	 * updateBy
	 */
	private String updateBy(Method method) {
		String name = method.getName();
		name = name.substring(KEY_UPDATE_BY.length(), name.length());
		List<String> propertyNames = Arrays.asList(name.split(LINKER));
		List<Property> byNames = this.model.getPropertiesByNames(propertyNames);
		List<Property> updateProperties = this.model.getPropertiesExcludeNames(propertyNames);
		if (byNames.size() == 0) {
			return "";
		}

		StringBuffer sbSql = new StringBuffer(256);
		sbSql.append("<update id='" + method.getName() + "' parameterType='" + this.model.getName() + "'  > ");
		sbSql.append("update " + model.getFullTableName());
		sbSql.append(" SET ");
		for (int i = 0, iSize = updateProperties.size(); i < iSize; i++) {
			Property property = updateProperties.get(i);
			sbSql.append(property.getColumnName() + " = #{" + property.getName() + "}");
			if (i + 1 < iSize) {
				sbSql.append(" , ");
			}
		}
		sbSql.append(" WHERE ");
		for (int i = 0, iSize = byNames.size(); i < iSize; i++) {
			Property property = byNames.get(i);
			sbSql.append(property.getColumnName() + " = #{" + property.getName() + "}");
			if (i + 1 < iSize) {
				sbSql.append(" AND ");
			}
		}
		sbSql.append("</update>");
		return sbSql.toString();
	}

}
