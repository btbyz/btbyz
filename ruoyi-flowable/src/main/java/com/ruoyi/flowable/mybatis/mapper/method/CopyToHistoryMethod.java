package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

@Slf4j
public class CopyToHistoryMethod extends MultiIdMethod {
    private boolean isMultiKey;
    private boolean isLog;

    public CopyToHistoryMethod(boolean isMultiKey, boolean isLog) {
        this.isMultiKey = isMultiKey;
        this.isLog = isLog;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        String sql = "<script>\nINSERT INTO %s (%s) %s\n</script>";
        String toInsert = "";
        StringBuffer selColumns = new StringBuffer();
        StringBuffer insColumns = new StringBuffer();
        tableInfo.getFieldList().forEach(
                col -> {
                    if ( insColumns.length() > 0) {
                        selColumns.append(',');
                        insColumns.append(',');
                    }
                    insColumns.append(col.getColumn());
                    if (StringUtils.equalsIgnoreCase("end_time",col.getColumn())) {
                        selColumns.append(" now() as ");
                    }
                    selColumns.append(col.getColumn());
                }
        );


        if (isMultiKey) {
            toInsert = "SELECT %s FROM %s WHERE " + createWhere(modelClass, tableInfo) + " %s";
            toInsert = String.format(toInsert, selColumns,
                    tableInfo.getTableName(), tableInfo.getLogicDeleteSql(true, true));
        } else {
            if ( tableInfo.havePK()) {
                insColumns.append(',').append(tableInfo.getKeyColumn());
                selColumns.append(',').append(tableInfo.getKeyColumn());
            }
            toInsert = String.format(SqlMethod.SELECT_BY_ID.getSql(),
                    selColumns,
                    tableInfo.getTableName(), tableInfo.getKeyColumn(), tableInfo.getKeyProperty(),
                    tableInfo.getLogicDeleteSql(true, true));
        }
        String targetTable = isLog ? (tableInfo.getTableName()+"_hist") : tableInfo.getTableName().replace("_curr","_hist");
        sql = String.format(sql,targetTable , insColumns, toInsert);
        log.info("copyToHistory {} : {} - {}",tableInfo.getTableName(),targetTable,sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, "copyToHistory", sqlSource, keyGenerator, null, null);
    }
}
