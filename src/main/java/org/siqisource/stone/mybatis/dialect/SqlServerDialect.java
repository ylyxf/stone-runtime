package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class SqlServerDialect extends AbstractDialect {

	@Override
	public String getDefualtSchema() {
		return "dbo";
	}

	@Override
	public String getKeySelector(KeyGenerator keyGenerator, Model model) {
		return null;
	}

	@Override
	public String getSelectKeyOrder() {
		return null;
	}

	@Override
	public String getLimitString(String sql, int skipResults, int maxResults) {
		return "";
	}

}
