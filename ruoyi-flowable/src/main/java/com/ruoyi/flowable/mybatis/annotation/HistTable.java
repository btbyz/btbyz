package com.ruoyi.flowable.mybatis.annotation;

import java.lang.annotation.*;

/**
 *  存在历史表标识。用于判断是否生成历史表相关脚本方法
 *  value默认为false。
 *  true代表此表存在历史日志记录表，历史表格式为当前表名跟上"_hist"。示例：mes_dict_data --> mes_dict_data_hist
 *  false代表此表存在历史表，当前表名格式应以"_curr"结尾，历史表以"_hist"结尾。例如：work_order_curr --> work_order_hist
 * @author yu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HistTable {
    boolean value() default false;
}
