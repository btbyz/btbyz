package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

@Slf4j
public class SelectByMultiIdMethod extends MultiIdMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String cWhere=createWhere(modelClass, tableInfo);
        if(cWhere==null){
            return null;
        }
        //
        String sql = "SELECT %s FROM %s WHERE "+cWhere+" %s";

        String method = "selectById";
        sql = String.format(sql, this.sqlSelectColumns(tableInfo, false), tableInfo.getTableName(), tableInfo.getLogicDeleteSql(true, true));
        log.info("selectById(multiId) {} - {}", tableInfo.getTableName(), sql);
        SqlSource sqlSource = new RawSqlSource(this.configuration, sql, Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, method, sqlSource, tableInfo);
    }
}
