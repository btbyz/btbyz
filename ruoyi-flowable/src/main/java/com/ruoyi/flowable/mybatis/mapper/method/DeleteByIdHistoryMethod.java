package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

@Slf4j
public class DeleteByIdHistoryMethod extends MultiIdMethod {
    private boolean isMultiKey;
    private boolean isLog;

    public DeleteByIdHistoryMethod(boolean isMultiKey, boolean isLog) {
        this.isMultiKey = isMultiKey;
        this.isLog = isLog;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n";
        String targetTable = isLog ? (tableInfo.getTableName()+"_hist") : tableInfo.getTableName().replace("_curr","_hist");
        if ( tableInfo.isWithLogicDelete()) {
            if (isMultiKey) {
                sql += "UPDATE only %s %s WHERE "+createWhere(modelClass, tableInfo)+" %s";
                sql = String.format(sql, targetTable, this.sqlLogicSet(tableInfo), tableInfo.getLogicDeleteSql(true, true));
            } else {
                sql += "UPDATE only %s %s WHERE %s=#{%s} %s";
                sql = String.format(sql, targetTable, this.sqlLogicSet(tableInfo),tableInfo.getKeyColumn(),tableInfo.getKeyProperty(), tableInfo.getLogicDeleteSql(true, true));
            }
        } else {
            if (isMultiKey) {
                sql += "DELETE FROM only %s WHERE " + createWhere(modelClass, tableInfo) + " %s";
                sql = String.format(sql,
                        targetTable, tableInfo.getLogicDeleteSql(true, true));
            } else {
                sql += String.format("DELETE FROM only %s WHERE %s=#{%s}",
                        targetTable, tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                        tableInfo.getLogicDeleteSql(true, true));
            }
        }
        sql += "\n</script>";
        log.info("deleteByIdFromHistory {} : {} - {}",tableInfo.getTableName(),targetTable,sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addDeleteMappedStatement(mapperClass, "deleteByIdFromHistory", sqlSource);
    }
}
