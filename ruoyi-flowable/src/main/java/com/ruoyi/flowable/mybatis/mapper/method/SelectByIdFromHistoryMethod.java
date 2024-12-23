package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

@Slf4j
public class SelectByIdFromHistoryMethod extends MultiIdMethod {
    private boolean isMultiKey;
    private boolean isLog;

    public SelectByIdFromHistoryMethod(boolean isMultiKey, boolean isLog) {
        this.isMultiKey = isMultiKey;
        this.isLog = isLog;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n";
        String targetTable = isLog ? (tableInfo.getTableName()+"_hist") : tableInfo.getTableName().replace("_curr","_hist");
        if (isMultiKey) {
            sql += "SELECT %s FROM only %s WHERE " + createWhere(modelClass, tableInfo) + " %s";
            sql = String.format(sql, this.sqlSelectColumns(tableInfo, false),
                    targetTable, tableInfo.getLogicDeleteSql(true, true));
        } else {
            sql += String.format(SqlMethod.SELECT_BY_ID.getSql(),
                    sqlSelectColumns(tableInfo, false),
                    "only " + targetTable, tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                    tableInfo.getLogicDeleteSql(true, true));
        }
        sql += " order by update_time desc limit 1\n</script>";
        log.info("selectByIdFromHistory {} : {} - {}",tableInfo.getTableName(),targetTable,sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, "selectByIdFromHistory", sqlSource, tableInfo);
    }
}
