package org.siqisource.stone.mybatis.plugins;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(value = { JdbcType.BOOLEAN })
@MappedTypes(Boolean.class)
public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {

	@Override
	public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String value = (String) cs.getObject(columnIndex);
		if (value == null) {
			return null;
		} else if ("F".equals(value)) {
			return Boolean.FALSE;
		} else if ("T".equals(value)) {
			return Boolean.TRUE;
		} else {
			return null;
		}
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
			throws SQLException {
		String value = null;
		if (Boolean.TRUE.equals(parameter)) {
			value = "T";
		} else if (Boolean.FALSE.equals(parameter)) {
			value = "F";
		}
		ps.setObject(i, value);
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = (String) rs.getObject(columnName);
		if (value == null) {
			return null;
		} else if ("F".equals(value)) {
			return Boolean.FALSE;
		} else if ("T".equals(value)) {
			return Boolean.TRUE;
		} else {
			return null;
		}
	}

	@Override
	public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String value = (String) rs.getObject(columnIndex);
		if (value == null) {
			return null;
		} else if ("F".equals(value)) {
			return Boolean.FALSE;
		} else if ("T".equals(value)) {
			return Boolean.TRUE;
		} else {
			return null;
		}
	}

}
