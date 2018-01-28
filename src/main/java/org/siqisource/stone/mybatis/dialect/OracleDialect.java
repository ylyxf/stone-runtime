package org.siqisource.stone.mybatis.dialect;

import org.siqisource.stone.mybatis.annotation.KeyGenerator;
import org.siqisource.stone.mybatis.model.Model;

public class OracleDialect extends AbstractDialect {

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
	public String getLimitString(String sql, int skipResults, int maxResults) {
		sql = sql.trim();

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

		pagingSelect.append(sql);

		pagingSelect.append(
				" ) row_ ) where rownum_ &gt; " + skipResults + " and rownum_ &lt;= " + (skipResults + maxResults));

		return pagingSelect.toString();
	}

}
