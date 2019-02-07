package org.siqisource.stone.mybatis.dialect;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class PostgreSqlDialect extends AbstractDialect {

	public static final Log logger = LogFactory.getLog(OracleDialect.class);

	@Override
	public String getLimitString(String sql, int offset, int limit, boolean useEacapeCharset) {
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

	@Override
	public void setClientInfo(Connection connection) {
		if (appUserId == null) {
			return;
		} else {
			String currentUserId = appUserId.get();
			currentUserId = currentUserId == null ? appUserId.anonymous() : currentUserId;

			Properties properties = new Properties();
			properties.put("ApplicationName", currentUserId);
			try {
				connection.setClientInfo(properties);
			} catch (SQLClientInfoException e) {
				logger.error("error seting app_user_id before stmt", e);
			}
		}
	}

}
