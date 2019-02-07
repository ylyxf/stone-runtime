package org.siqisource.stone.mybatis.dialect;

import java.sql.Connection;
import java.sql.SQLClientInfoException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class OracleDialect extends AbstractDialect {

	public static final Log logger = LogFactory.getLog(OracleDialect.class);

	@Override
	public String getDefualtSchema() {
		return null;
	}

	@Override
	public String getKeySelector(KeyGenerator keyGenerator, Model model) {
		if (keyGenerator == KeyGenerator.uuid) {
			return "select sys_guid() from dual";
		} else if (keyGenerator == KeyGenerator.sequence) {
			String sequence = model.getKeySequence();
			if (sequence == null || "".equals(sequence)) {
				String tableName = model.getTableName().toUpperCase();
				sequence = tableName + "_SEQ";
			}
			return "select " + sequence + ".NEXTVAL from dual";
		} else {
			throw new RuntimeException("only uuid and sequence  is supported for selectKey of oracle  ");
		}
	}

	@Override
	public String getSelectKeyOrder() {
		return "BEFORE";
	}

	@Override
	public String getLimitString(String sql, int skipResults, int maxResults, boolean useEacapeCharset) {

		String gt = ">";
		String lt = "<";

		if (useEacapeCharset) {
			gt = "&gt;";
			lt = "&lt;";
		}

		sql = sql.trim();

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

		pagingSelect.append(sql);

		pagingSelect.append(" ) row_ ) where rownum_ " + gt + " " + skipResults + " and rownum_  " + lt + "= "
				+ (skipResults + maxResults));

		return pagingSelect.toString();
	}

	@Override
	public void setClientInfo(Connection connection) {
		if (appUserId == null) {
			return;
		} else {
			String currentUserId = appUserId.get();
			currentUserId = currentUserId == null ? appUserId.anonymous() : currentUserId;

			Properties properties = new Properties();
			properties.put("app_user_id", currentUserId);
			try {
				connection.setClientInfo(properties);
			} catch (SQLClientInfoException e) {
				logger.error("error seting app_user_id before stmt", e);
			}
		}
	}

}
