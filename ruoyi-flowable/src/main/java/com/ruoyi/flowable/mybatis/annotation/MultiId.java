package com.ruoyi.flowable.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 复合主键标识
 * @author yu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MultiId {
    String value() default "";
}
