package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

@Slf4j
public class UpdateByMultiIdMethod extends MultiIdMethod {

    @Override
    protected String createWhere(Class<?> modelClass, TableInfo tableInfo){
        Map<String, String> idMap = idMaps(modelClass,tableInfo);
        if(idMap.isEmpty()){
            log.info("entity {} not contain MppMultiId anno", modelClass.getName());
            return null;
        }
        StringBuilder sb=new StringBuilder("");
        idMap.forEach((attrName, colName)->{
            if(sb.length() > 0){
                sb.append(" and ");
            }
            sb.append(colName).append("=").append("#{et.").append(attrName).append("}");
        });
        return sb.toString();
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String cWhere=createWhere(modelClass, tableInfo);
        if(cWhere==null){
            return null;
        }
        String methodName="updateById";
        boolean logicDelete = tableInfo.isWithLogicDelete();
        String sqlTemp="<script>\nUPDATE %s %s WHERE "+cWhere+" %s\n</script>";
        String additional = this.optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sql = String.format(sqlTemp, tableInfo.getTableName(), this.sqlSet(logicDelete, false, tableInfo, false, "et", "et."), additional);
        log.info("updateById(multiId) {} - {}", tableInfo.getTableName(), sql);
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }
}
