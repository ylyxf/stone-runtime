package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class PostgreSqlDialect extends AbstractDialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ") + " limit " + limit + " offset " + offset;
		return sql;
	}

	@Override
	public String getKeySelector(KeyGenerator keyGenerator, Model model) {
		if (keyGenerator == KeyGenerator.uuid) {
			return "select md5(random()::text || clock_timestamp()::text)::uuid as id";
		} else if (keyGenerator == KeyGenerator.sequence) {
			String sequence = model.getKeySequence();
			return "select nextval('" + sequence + "') as id ";
		} else {
			throw new RuntimeException("only uuid and sequence  is supported for selectKey of postgresql  ");
		}
	}

	@Override
	public String getSelectKeyOrder() {
		return "BEFORE";
	}

	@Override
	public String getDefualtSchema() {
		return "public";
	}

}
