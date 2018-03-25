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
		if (keyGenerator == KeyGenerator.uuid) {
			return "select NEWID()";
		} else {
			throw new RuntimeException("only uuid and sequence  is supported for selectKey of SqlServer  ");
		}
	}

	@Override
	public String getSelectKeyOrder() {
		return "BEFORE";
	}

	@Override
	public String getLimitString(String sql, int skipResults, int maxResults,boolean useEacapeCharset) {
		return "";
	}

}
