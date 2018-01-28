package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.utils.NameConverter;

public abstract class AbstractDialect implements Dialect {

	public String convertColumnToProperty(String columnName) {
		return NameConverter.underlineToCamelFirstLower(columnName);
	}

}
