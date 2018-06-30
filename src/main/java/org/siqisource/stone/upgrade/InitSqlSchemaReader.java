package org.siqisource.stone.upgrade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.siqisource.stone.utils.ResourceUtil;

public class InitSqlSchemaReader {

	public static String TABLE_EXECUTED_SQL = "STRT_EXECUTED";

	public static String getInitSchemaFile(Properties properties, String appId) throws Exception {
		System.out.println("initSqlFolder:::::");
		// 连接数据库
		String url = properties.getProperty("spring.datasource.url");
		String username = properties.getProperty("spring.datasource.username");
		String password = properties.getProperty("spring.datasource.password");
		String driver = properties.getProperty("spring.datasource.driver-class-name");
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, username, password);

		// 尝试读取是否存在配置表
		Set<String> executedSqlSet = new HashSet<String>();
		try {
			Statement tableStatement = conn.createStatement();
			String sql = "select SQL_NAME from STRT_EXECUTED where APPID ='" + appId + "' order by EXECUTE_TIME asc";
			ResultSet sqlRs = tableStatement.executeQuery(sql);
			while (sqlRs.next()) {
				String executedSql = sqlRs.getString(1);
				executedSqlSet.add(executedSql);
			}
			sqlRs.close();
			tableStatement.close();
		} catch (Exception e) {
			Statement tableStatement = conn.createStatement();
			String sql = "create table STRT_EXECUTED (APPID varchar(128),SQL_NAME varchar(128),EXECUTE_TIME date,primary key(APPID,SQL_NAME))";
			tableStatement.execute(sql);
			tableStatement.close();
		}

		// 读取jar包中的classes资源
		ResourceUtil resourceUtil = new ResourceUtil();
		List<String> sqlFiles = resourceUtil.listResources("initsql");
		for (String sqlFile : sqlFiles) {
			System.out.println("sqlFile:::::" + sqlFile);
		}
		// 剔除掉已执行的sql
		Iterator<String> sqlFileIterator = sqlFiles.iterator();
		while (sqlFileIterator.hasNext()) {
			String sqlFile = sqlFileIterator.next();
			if (executedSqlSet.contains(sqlFile)) {
				sqlFileIterator.remove();
			}
		}

		String initSchema = "";
		for (String sqlFile : sqlFiles) {
			initSchema += sqlFile + ",";
		}
		if (sqlFiles.size() > 0) {
			initSchema = initSchema.substring(0, initSchema.length() - 1);
		}
		return initSchema;
	}

}
