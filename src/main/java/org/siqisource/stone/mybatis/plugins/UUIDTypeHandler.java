package org.siqisource.stone.mybatis.plugins;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedJdbcTypes(value = {JdbcType.OTHER })
@MappedTypes(UUID.class)
public class UUIDTypeHandler extends BaseTypeHandler<UUID> {

	@Override
	public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return (UUID) cs.getObject(columnIndex);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setObject(i, parameter);
	}

	@Override
	public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return (UUID) rs.getObject(columnName);// TODO Auto-generated method
												// stub
	}

	@Override
	public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return (UUID) rs.getObject(columnIndex);// TODO Auto-generated method
												// stub
	}
}
