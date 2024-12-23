package com.ruoyi.flowable.config;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ruoyi.flowable.config.annotation.ExcelConverterField;

import java.lang.reflect.Field;

public class EntityToStringConverter implements Converter<Object> {

    @Override
    public Class<Object> supportJavaTypeKey() {
        return Object.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new WriteCellData<>("");
        }

        StringBuilder result = new StringBuilder();
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String prefix = "";
                Object fieldValue = field.get(value);
                if (field.isAnnotationPresent(ExcelConverterField.class)) {
                    ExcelConverterField excelField = field.getAnnotation(ExcelConverterField.class);
                    prefix = excelField.value();
                }
                if (!prefix.isEmpty() && fieldValue != null) {
                    result.append(prefix).append("：").append(fieldValue).append("\n");
                } else if (fieldValue != null) {
                    result.append(fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (result.toString().endsWith("\n")) {
            String resultString = result.substring(0, result.length() - 1);
        }
        return new WriteCellData<>(result.toString().trim());
    }
}