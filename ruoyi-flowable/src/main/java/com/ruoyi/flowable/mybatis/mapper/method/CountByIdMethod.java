package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

@Slf4j
public class CountByIdMethod extends MultiIdMethod {
    private boolean isMultiKey;

    public CountByIdMethod(boolean isMultiKey) {
        this.isMultiKey = isMultiKey;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n";
        if (isMultiKey) {
            sql += "SELECT count(1) FROM %s WHERE %s %s";
            sql = String.format(sql,
                    tableInfo.getTableName(),createWhere(modelClass, tableInfo), tableInfo.getLogicDeleteSql(true, true));
        } else {
            sql += String.format("SELECT count(1) FROM %s WHERE %s=#{%s} %s",
                    tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                    tableInfo.getLogicDeleteSql(true, true));
        }
        sql += "\n</script>";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, "countById", sqlSource, Integer.class);
    }
}
