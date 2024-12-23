package com.ruoyi.flowable.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.flowable.config.EntityToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 系统运维日志对象 mes_operation_log
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MesOperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelIgnore
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 系统 */
    @ExcelProperty("系统")
    private String systemModule;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("日期")
    @DateTimeFormat("yyyy/M/D")
    private Date fullDate;

    /** 年 */
    @ExcelProperty("年")
    private Long year;

    /** 月 */
    @ExcelProperty("月")
    private Long month;

    /** 周 */
    @ExcelProperty("周")
    private Long weekOfYear;

    /** 工厂 */
    @ExcelProperty("工厂")
    private String factory;

    /** 异常名称 */
    @ColumnWidth(25)
    @ExcelProperty("异常名称")
    private String problemName;

    /** 用户 */
    @ExcelProperty("用户")
    private String requirementUser;

    /** 分类 */
    @ExcelProperty("分类")
    private String problemType;

    /** 异常描述 */
    @ColumnWidth(50)
    @ExcelProperty(value = "异常描述",converter = EntityToStringConverter.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProblemDescVo problemDesc;

    /** 次数 */
    @ExcelProperty("次数")
    private Long times;

    /** 根因分析 */
    @ColumnWidth(25)
    @ExcelProperty("根因分析")
    private String rootCause;

    /** 临时措施 */
    @ColumnWidth(25)
    @ExcelProperty("临时措施")
    private String temporaryMeasures;

    /** 长期措施 */
    @ColumnWidth(25)
    @ExcelProperty("长期措施")
    private String longTermMeasures;

    /** 负责人 */
    @ExcelProperty("负责人")
    private String responsiblePerson;

    /** 跟进事项 */
    @ColumnWidth(25)
    @ExcelProperty("跟进事项")
    private String followUpMatters;

    /** 纳期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("纳期")
    @DateTimeFormat("yyyy/M/D")
    private Date deliveryTime;

    /** 状态 */
    @ExcelProperty("状态")
    private String status;

    /** CAR */
    @ColumnWidth(25)
    @ExcelProperty("car")
    private String car;

    /** 云文档 */
    @ColumnWidth(25)
    @ExcelProperty("云文档")
    private String docUrl;

    /** 备注 */
    @ExcelProperty("备注")
    private String remark;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createUid;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createUname;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUid;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUname;

    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 开始时间 */
    @ExcelIgnore
    @TableField(exist = false)
    private Date beginTime;

    /** 结束时间 */
    @ExcelIgnore
    @TableField(exist = false)
    private Date endTime;

    /** 计算按维度分类的数量所用到的维度名 */
    @TableField(exist = false)
    @ExcelIgnore
    private String dimensionName;

    /**
     * 汇总数量
     */
    @ExcelIgnore
    @TableField(exist = false)
    private Long count;

    // 根据日期更新 年 月 周
    public void updateFromFullDate() {
        if (fullDate!= null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(fullDate);
            this.year = (long) calendar.get(Calendar.YEAR);
            this.month = (long) calendar.get(Calendar.MONTH) + 1;
            this.weekOfYear = (long) calendar.get(Calendar.WEEK_OF_YEAR);
        }
    }
}
