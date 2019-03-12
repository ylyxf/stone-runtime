package org.siqisource.stone.mybatis.dialect;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class H2Dialect extends AbstractDialect {

	public static final Log logger = LogFactory.getLog(OracleDialect.class);

	@Override
	public String getLimitString(String sql, int offset, int limit, boolean useEacapeCharset) {
		sql = sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ") + " limit " + limit + " offset " + offset;
		return sql;
	}

	@Override
	public String getKeySelector(KeyGenerator keyGenerator, Model model) {
		if (keyGenerator == KeyGenerator.uuid) {
			return "select RANDOM_UUID() as id";
		} else if (keyGenerator == KeyGenerator.sequence) {
			return "";
		} else {
			throw new RuntimeException("only uuid  is supported for selectKey of h2db  ");
		}
	}

	@Override
	public String getSelectKeyOrder() {
		return "AFTER";
	}

	@Override
	public String getDefualtSchema() {
		return "PUBLIC";
	}

	@Override
	public void setClientInfo(Connection connection) {

	}

}
