package com.ruoyi.flowable.mybatis.typeHandle;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class StringArrayTypeHandle extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        Array array = preparedStatement.getConnection().createArrayOf("VARCHAR", strings.toArray());
        preparedStatement.setArray(i, array);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return this.extractArray(resultSet.getArray(s));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return this.extractArray(resultSet.getArray(i));
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return this.extractArray(callableStatement.getArray(i));
    }

    protected List<String> extractArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        } else {
            Object result = array.getArray();
            array.free();
            return Arrays.asList((String[]) result);
        }
    }
}
