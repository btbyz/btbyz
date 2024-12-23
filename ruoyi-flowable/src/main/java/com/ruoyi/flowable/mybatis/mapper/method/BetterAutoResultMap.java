package com.ruoyi.flowable.mybatis.mapper.method;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BetterAutoResultMap extends AbstractMethod {

    /**
     * 强制重设TableInfo的resultMap属性
     */
    static Field ResultMapOfTableInfo;

    /**
     * 强制重设TableInfo的autoInitResultMap属性
     */
    static Field AutoInitResultMapOfTableInfo;
    static {
        try {
            ResultMapOfTableInfo = TableInfo.class.getDeclaredField("resultMap");
            ResultMapOfTableInfo.setAccessible(true);
            AutoInitResultMapOfTableInfo = TableInfo.class.getDeclaredField("autoInitResultMap");
            AutoInitResultMapOfTableInfo.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.error("autoInitResultMap init fail",e);
        }
    }

    /**
     * 注入自定义 MappedStatement
     *
     * 当实体类没有指定autoResultMap和resultMap时, 即可使用该方法自动注入ResultMap
     *
     * @param mapperClass mapper 接口
     * @param modelClass  mapper 泛型
     * @param tableInfo   数据库表反射信息
     * @return MappedStatement
     */
    @SneakyThrows
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        if (!tableInfo.isAutoInitResultMap() && tableInfo.getResultMap() == null) {

            // 只要字段中指定了TypeHandler即自动生成ResultMap
            if (tableInfo.getFieldList().stream()
                    .anyMatch(f -> f.getTypeHandler() != null && f.getTypeHandler() != UnknownTypeHandler.class)) {

                // 生成ResultMap
                ResultMap resultMap = generatorResultMap(tableInfo);
                configuration.addResultMap(resultMap);

                // 将ResultMap属性设置到TableInfo
                ResultMapOfTableInfo.set(tableInfo, resultMap.getId());
                AutoInitResultMapOfTableInfo.set(tableInfo, true);
            }
        }
        return null;
    }

    /**
     * 构建 resultMap
     */
    ResultMap generatorResultMap(TableInfo tableInfo) {
        String resultMapId = tableInfo.getCurrentNamespace() + DOT + "mybatis-plus" + UNDERSCORE + tableInfo.getEntityType().getSimpleName();
        List<ResultMapping> resultMappings = tableInfo.getFieldList().stream().filter(f -> f.getTypeHandler() != null &&
                f.getTypeHandler() != UnknownTypeHandler.class).map(this::getResultMapping).collect(Collectors.toList());
        return new ResultMap.Builder(configuration, resultMapId, tableInfo.getEntityType(), resultMappings).build();
    }

    /**
     * 构建 resultMapping （只针对typeHandler的字段）
     *
     * @param tableFieldInfo
     * @return
     */
    ResultMapping getResultMapping(TableFieldInfo tableFieldInfo) {
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, tableFieldInfo.getProperty(),
                this.isNotColumnName(tableFieldInfo.getColumn()) ? tableFieldInfo.getProperty() : tableFieldInfo.getColumn(),
                tableFieldInfo.getPropertyType());
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        JdbcType jdbcType = tableFieldInfo.getJdbcType();
        if (jdbcType != null && jdbcType != JdbcType.UNDEFINED) {
            builder.jdbcType(jdbcType);
        }
        TypeHandler<?> typeHandlerMapped = registry.getMappingTypeHandler(tableFieldInfo.getTypeHandler());
        if (typeHandlerMapped == null) {
            typeHandlerMapped = registry.getInstance(tableFieldInfo.getPropertyType(), tableFieldInfo.getTypeHandler());
        }
        builder.typeHandler(typeHandlerMapped);
        return builder.build();
    }

    private boolean isNotColumnName(String column) {
        return column.contains(StringPool.DOT) || StringUtils.isNotColumnName(column);
    }
}
