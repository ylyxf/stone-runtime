package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public interface Dialect {

	public String getDefualtSchema();

	public String getKeySelector(KeyGenerator keyGenerator, Model model);

	public String getSelectKeyOrder();

	public String getLimitString(String sql, int skipResults, int maxResults,boolean useEacapeCharset);
	
	public String convertColumnToProperty(String column);

}
