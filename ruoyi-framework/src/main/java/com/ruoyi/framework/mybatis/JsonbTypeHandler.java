package com.ruoyi.framework.mybatis;

import com.alibaba.fastjson2.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({ Object.class })
public class JsonbTypeHandler extends BaseTypeHandler<Object> {

    //报错的话，请将pom文件的引入graphql的地方<version></version>标签注释掉
    private static final PGobject jsonObject = new PGobject();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        if (ps != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(JSON.toJSONString(parameter));
            ps.setObject(i, jsonObject);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object object = rs.getObject(columnName);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object object = rs.getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object object = cs.getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return object.toString();
    }

}
