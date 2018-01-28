package org.siqisource.stone.upgrade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class InitSqlSchemaReader {

	public static String SQL_TEST_VERSION = "";

	public static String SQL_VERSION_IN_DB = "";

	public static final LinkedHashMap<String, String> VERSION_SQL_STORE = new LinkedHashMap<String, String>();

	public static String getInitSchemaFile(Properties properties, String appVersion) throws Exception {

		List<String> sqlFiles = new ArrayList<String>();

		// 连接数据库
		String url = properties.getProperty("spring.datasource.url");
		String username = properties.getProperty("spring.datasource.username");
		String password = properties.getProperty("spring.datasource.password");
		String driver = properties.getProperty("spring.datasource.driver-class-name");
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, username, password);

		// 尝试读取是否存在配置表
		Statement tableStatement = conn.createStatement();
		ResultSet tableRs = tableStatement.executeQuery(SQL_TEST_VERSION);
		tableRs.next();
		Integer tableCount = tableRs.getInt(1);
		tableRs.close();
		tableStatement.close();

		if (tableCount == 0) {
			Set<Entry<String, String>> entrySet = VERSION_SQL_STORE.entrySet();
			for (Entry<String, String> entry : entrySet) {
				sqlFiles.add(entry.getValue());
			}
		} else {
			// 从配置表中读取当前版本
			String currentVersion = "";
			Statement versionStatement = conn.createStatement();
			ResultSet versionRs = versionStatement.executeQuery(SQL_VERSION_IN_DB);
			if (versionRs.next()) {
				currentVersion = versionRs.getString("version");
			} else {
				throw new Exception("无法查询到URPLUS版本号");
			}
			versionRs.close();
			versionStatement.close();
			conn.close();

			if (currentVersion == null || "".equals(currentVersion)) {
				throw new Exception("无法查询到URPLUS");
			}

			if (!VERSION_SQL_STORE.containsKey(currentVersion)) {
				throw new Exception("URPLUS版本号'" + currentVersion + "'未定义");
			}

			if (greater(currentVersion, appVersion)) {
				throw new Exception("数据库中的URPLUS版本号" + currentVersion + "高于当前程序版本" + appVersion + "，请下载最新版小助手'");
			}
			if (!appVersion.equals(currentVersion)) {
				boolean foundVersionInDefine = false;
				Set<Entry<String, String>> entrySet = VERSION_SQL_STORE.entrySet();
				for (Entry<String, String> entry : entrySet) {
					if (currentVersion.equals(entry.getKey())) {
						foundVersionInDefine = true;
						continue;
					}
					if (foundVersionInDefine) {
						sqlFiles.add(entry.getValue());
					}
				}
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

	public static boolean greater(String version1, String version2) throws Exception {
		String[] version1Array = version1.split("[.]");
		String[] version2Array = version2.split("[.]");

		if (version1Array.length < 3) {
			throw new Exception("版本号异常:" + version1);
		}

		if (version2Array.length < 3) {
			throw new Exception("版本号异常" + version2);
		}

		int version1Main = 0;
		int version1Middle = 0;
		int version1Third = 0;
		try {
			version1Main = Integer.parseInt(version1Array[0]);
			version1Middle = Integer.parseInt(version1Array[1]);
			version1Third = Integer.parseInt(version1Array[2]);
		} catch (NumberFormatException e) {
			throw new Exception("版本号异常:x.y.z.other中，x/y/z应该为数字" + version1);
		}

		int version2Main = 0;
		int version2Middle = 0;
		int version2Third = 0;
		try {
			version2Main = Integer.parseInt(version2Array[0]);
			version2Middle = Integer.parseInt(version2Array[1]);
			version2Third = Integer.parseInt(version2Array[2]);
		} catch (NumberFormatException e) {
			throw new Exception("版本号异常:x.y.z.other中，x/y/z应该为数字" + version2);
		}

		if (version1Main > version2Main) {
			return true;
		} else if (version1Main == version2Main) {
			if (version1Middle > version2Middle) {
				return true;
			} else if (version1Middle == version2Middle) {
				if (version1Third > version2Third) {
					return true;
				}
			}
		}
		return false;
	}

}
