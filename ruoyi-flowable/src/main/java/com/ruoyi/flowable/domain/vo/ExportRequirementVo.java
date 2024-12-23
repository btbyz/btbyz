package com.ruoyi.flowable.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
@Data
public class ExportRequirementVo {

    @ExcelProperty("项目名称")
    @TableField(exist = false)
    private String projectName;

    @ExcelProperty("需求编号")
    private String requirementCode;

    @ExcelProperty("需求名称")
    private String requirementName;

    @ExcelProperty("责任人")
    private String createUname;

    @ExcelProperty("需求属性")
    private String requirementType;

    @ExcelProperty("需求评审方式")
    private String reviewMode;

    @ExcelProperty("需求评审状态")
    private String reviewStatus;

    @ExcelProperty("需求状态")
    private String status;

    @ExcelProperty("需求总耗工时")
    @TableField(exist = false)
    private double totalTime;

    @ExcelProperty("服务目录")
    private String systemModule;

    @ExcelProperty("需求模块")
    private String requirementModule;

    @ExcelProperty("需求描述")
    private String requirementDesc;

    @ExcelProperty("oa单号")
    private String requirementOaId;

    @ExcelProperty("需求用户")
    private String requirementUser;

    @ExcelProperty("需求收益类型")
    private String incomeType;

    @ExcelProperty("收益")
    private String income;

    @ExcelProperty("重要程度(H/L)")
    private String importantLevel;

    @ExcelProperty("紧急程度(U/N)")
    private String urgencyLevel;

    @ExcelProperty("预估月使用次数")
    private String monthlyUsageFrequency;

    @ExcelProperty("需求部门")
    private String requirementDept;

    @ExcelIgnore
    private Long deptId;

    @ExcelProperty("责任部门")
    private String deptName;

    @ExcelProperty("需求类型")
    private String attribute;

    @ExcelProperty("计划开始时间")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date beginTime;

    @ExcelProperty("计划结束时间")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private Date endTime;

    @ExcelProperty("实际开始时间")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date actualBeginTime;

    @ExcelProperty("实际结束时间")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date actualEndTime;

    @ExcelProperty("交付状态")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String deliveryStatus;

}
