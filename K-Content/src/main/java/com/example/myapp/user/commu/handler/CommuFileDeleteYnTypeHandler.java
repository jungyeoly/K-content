package com.example.myapp.user.commu.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.example.myapp.user.commu.status.CommuFileDeleteYn;

public class CommuFileDeleteYnTypeHandler extends BaseTypeHandler<CommuFileDeleteYn> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, CommuFileDeleteYn parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getCode());
	}

	@Override
	public CommuFileDeleteYn getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String code = rs.getString(columnName);
		return CommuFileDeleteYn.fromCode(code);
	}

	@Override
	public CommuFileDeleteYn getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String code = rs.getString(columnIndex);
		return CommuFileDeleteYn.fromCode(code);
	}

	@Override
	public CommuFileDeleteYn getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String code = cs.getString(columnIndex);
		return CommuFileDeleteYn.fromCode(code);
	}
}
