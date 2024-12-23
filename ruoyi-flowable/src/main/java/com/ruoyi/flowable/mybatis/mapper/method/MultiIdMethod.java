package com.ruoyi.flowable.mybatis.mapper.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.ruoyi.flowable.mybatis.annotation.MultiId;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class MultiIdMethod extends AbstractMethod {

    protected String getCol(List<TableFieldInfo> fieldList, String attrName){
        for(TableFieldInfo tableFieldInfo: fieldList){
            String prop=tableFieldInfo.getProperty();
            if(prop.equals(attrName)){
                return tableFieldInfo.getColumn();
            }
        }
        throw new RuntimeException("not found column for "+attrName);
    }

    protected Map<String,String> idMaps(Class<?> modelClass, TableInfo tableInfo){
        List<TableFieldInfo> fieldList=tableInfo.getFieldList();
        Field[] fieldArray= modelClass.getDeclaredFields();
        Map<String, String> idMap=new HashMap<>(16);
        for(Field field: fieldArray){
            MultiId mppMultiId = field.getAnnotation(MultiId.class);
            if(mppMultiId!=null){
                String attrName = field.getName();
                String colName = getCol(fieldList, attrName);
                idMap.put(attrName, colName);
            }
        }
        return idMap;
    }


    protected String createWhere(Class<?> modelClass, TableInfo tableInfo){
        Map<String, String> idMap = idMaps(modelClass,tableInfo);
        if(idMap.isEmpty()){
            log.info("entity {} not contain MesMultiId anno", modelClass.getName());
            return null;
        }
        StringBuilder sb=new StringBuilder("");
        idMap.forEach((attrName, colName)->{
            if(sb.length() >0) {
                sb.append(" and ");
            }
            sb.append(colName).append("=").append("#{").append(attrName).append("}");
        });
        return sb.toString();
    }
}
