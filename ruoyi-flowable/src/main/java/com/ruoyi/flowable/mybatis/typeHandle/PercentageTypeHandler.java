package com.ruoyi.flowable.mybatis.typeHandle;

import com.ruoyi.common.utils.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 1109713
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(BigDecimal.class)
public class PercentageTypeHandler extends BaseTypeHandler<BigDecimal> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, BigDecimal bigDecimal, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, bigDecimal.multiply(new BigDecimal("100")) + "%");
    }

    @Override
    public BigDecimal getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getBigDecimal(resultSet.getString(s));
    }

    @Override
    public BigDecimal getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getBigDecimal(resultSet.getString(i));
    }

    @Override
    public BigDecimal getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getBigDecimal(callableStatement.getString(i));
    }


    private BigDecimal getBigDecimal(String str) throws SQLException {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (str.contains("%")) {
            str = str.substring(0, str.length() - 1);
            BigDecimal value = new BigDecimal(str);
            if (value.equals(BigDecimal.ZERO)) {
                return BigDecimal.ZERO;
            }
            return value.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        } else {
            return new BigDecimal(str);
        }
    }
}
