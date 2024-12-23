package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询历史列表
 * @author yu
 */
@Slf4j
public class SelectListFromHistoryMethod extends MultiIdMethod {
    private boolean isMultiKey;
    private boolean isLog;

    public SelectListFromHistoryMethod(boolean isMultiKey, boolean isLog) {
        this.isMultiKey = isMultiKey;
        this.isLog = isLog;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String listSql = "<script> SELECT %s FROM %s %s";
        String targetTable = isLog ? (tableInfo.getTableName() + "_hist") : tableInfo.getTableName().replace("_curr", "_hist");
        String sql = String.format(listSql, sqlSelectColumns(tableInfo, false), targetTable,
            customSqlWhereEntityWrapper(tableInfo));
        sql += isLog ? " order by update_time desc\n</script>" : " order by rec_time desc\n</script>";
        log.info("selectListFromHistory {} : {} - {}", tableInfo.getTableName(), targetTable, sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, "selectListFromHistory", sqlSource, tableInfo);
    }


    protected String customSqlWhereEntityWrapper(TableInfo table) {
        String sqlScript = table.getAllSqlWhere(false, true, null);
        sqlScript += NEWLINE;
        sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
        return sqlScript;
    }
}
