package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.utils.NameConverter;

public abstract class AbstractDialect implements Dialect {

	protected AppUserId appUserId;

	public String convertColumnToProperty(String columnName) {
		return NameConverter.underlineToCamelFirstLower(columnName);
	}

	public void setAppUserId(AppUserId appUserId) {
		this.appUserId = appUserId;
	}

}
