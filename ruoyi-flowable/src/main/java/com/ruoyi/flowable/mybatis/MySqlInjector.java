package com.ruoyi.flowable.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.ruoyi.flowable.mybatis.annotation.HistTable;
import com.ruoyi.flowable.mybatis.annotation.MultiId;
import com.ruoyi.flowable.mybatis.mapper.method.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 将自定义sql注入mybatis框架
 *
 * @author yu
 */
@Slf4j
@Component
public class MySqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = new ArrayList<>();
        methodList.add(new BetterAutoResultMap());
        methodList.addAll(super.getMethodList(mapperClass,tableInfo));
        boolean isMultiKey = isMultiKey(tableInfo.getEntityType());
        methodList.add(new CountByIdMethod(isMultiKey));
        HistTable histTable = tableInfo.getEntityType().getAnnotation(HistTable.class);
        if (histTable != null) {
            methodList.add(new CopyToHistoryMethod(isMultiKey, histTable.value()));
            methodList.add(new SelectByIdFromHistoryMethod(isMultiKey, histTable.value()));
            methodList.add(new SelectListFromHistoryMethod(isMultiKey, histTable.value()));
            methodList.add(new InsertIntoHistoryMethod(isMultiKey, histTable.value()));
            methodList.add(new DeleteByIdHistoryMethod(isMultiKey, histTable.value()));
        }
        if (isMultiKey) {
            log.info(String.format("%s ,found @MultiId annotation", tableInfo.getEntityType()));
            methodList.add(new DeleteByMultiIdMethod());
            methodList.add(new SelectByMultiIdMethod());
            methodList.add(new UpdateByMultiIdMethod());
        }
        return methodList;
    }

    private boolean isMultiKey(Class<?> mapper) {
        for (Field field : mapper.getDeclaredFields()) {
            if (field.getAnnotation(MultiId.class) != null ) {
                return  true;
            }
        }
        return false;
    }

}
