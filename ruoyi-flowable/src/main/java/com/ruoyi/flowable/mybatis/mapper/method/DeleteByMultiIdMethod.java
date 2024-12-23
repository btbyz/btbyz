package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author yu
 */
@Slf4j
public class DeleteByMultiIdMethod extends MultiIdMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String cWhere=createWhere(modelClass, tableInfo);
        if(cWhere==null){
            return null;
        }
        String methodName="deleteById";
        SqlSource sqlSource;
        if (tableInfo.isWithLogicDelete()) {
            String sqlTemp="<script>\nUPDATE %s %s WHERE "+cWhere+" %s\n</script>";
            String sql = String.format(sqlTemp, tableInfo.getTableName(), this.sqlLogicSet(tableInfo), tableInfo.getLogicDeleteSql(true, true));
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
            log.info("deleteById(multiId) {} - {}", tableInfo.getTableName(), sql);
            return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
        } else {
            String sqlTemp = "DELETE FROM %s WHERE "+cWhere;
            String sql = String.format(sqlTemp, tableInfo.getTableName());
            log.info("deleteById(multiId) {} - {}", tableInfo.getTableName(), sql);
            sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
            return this.addDeleteMappedStatement(mapperClass, methodName, sqlSource);
        }
    }
}
