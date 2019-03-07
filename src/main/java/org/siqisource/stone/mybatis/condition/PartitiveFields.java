package org.siqisource.stone.mybatis.condition;

import java.util.HashMap;
import java.util.Map;

import org.siqisource.stone.mybatis.utils.NameConverter;

public class PartitiveFields extends HashMap<String, Object> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3865131202288816404L;

	/**
	 * prepare Fields to insert
	 *
	 */
	public void prepareInsertSql() {
		if (this.get("_insertFields") == null) {

			StringBuffer sbInsertFields = new StringBuffer();
			StringBuffer sbInsertValues = new StringBuffer();
			boolean firstFiled = true;
			for (Map.Entry<String, Object> entry : this.entrySet()) {
				String key = entry.getKey();
				if (!firstFiled) {
					sbInsertFields.append(" , ");
					sbInsertValues.append(" , ");
				}
				sbInsertFields.append(NameConverter.camelToUnderlineLower(key));

				sbInsertValues.append("#{fields[");
				sbInsertValues.append(key.trim());
				sbInsertValues.append("]}");

				firstFiled = false;
			}
			this.put("_insertFields", sbInsertFields.toString());
			this.put("_insertValues", sbInsertValues.toString());
		}
	}

	/**
	 * prepare sql to update
	 *
	 */
	public void prepareUpdateSql() {
		if (this.get("_updateSql") == null) {
			StringBuffer sbUpdateSql = new StringBuffer();
			boolean firstFiled = true;
			for (Map.Entry<String, Object> entry : this.entrySet()) {
				String key = entry.getKey();
				if (!firstFiled) {
					sbUpdateSql.append(" , ");
				}

				if (key.contains("+=")) {
					String column = NameConverter.camelToUnderlineLower(key.replace("+=", ""));
					sbUpdateSql.append(column).append(" = ").append(column).append(" + ");
					sbUpdateSql.append("#{fields[");
					sbUpdateSql.append(key.trim());
				} else {
					sbUpdateSql.append(NameConverter.camelToUnderlineLower(key));
					sbUpdateSql.append(" = #{fields[");
					sbUpdateSql.append(key.trim());
				}
				sbUpdateSql.append("]}");
				firstFiled = false;
			}
			this.put("_updateSql", sbUpdateSql.toString());
		}
	}

	public int getIntValueByKey(String key) {
		if (this.get(key) == null) {
			return 0;
		} else {
			return Integer.parseInt(this.get(key).toString());
		}
	}

}
