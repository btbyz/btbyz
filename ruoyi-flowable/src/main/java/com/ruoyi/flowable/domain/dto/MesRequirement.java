package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 需求池 mes_requirement
 *
 * @author ruoyi
 * @date 2024-04-08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MesRequirement extends BaseEntityDto
{
    private static final long serialVersionUID = 1L;

    /** 需求编号 */
    @TableId
    private String requirementCode;

    /** 需求名称 */
    private String requirementName;

    /** 需求模块 */
    private String requirementModule;

    /** 需求描述 */
    private String requirementDesc;

    /** oa申请单号 */
    private String requirementOaId;

    /** 所属项目 */
    private String projectCode;

    /** 需求状态 */
    private String status;

    /** 发布版本 */
    private String releaseVersion;

    /** 需求用户 */
    private String requirementUser;

    /** 收益 */
    private String income;

    /** 重要程度 */
    private String importantLevel;

    /** 紧急程度 */
    private String urgencyLevel;

    /** 预估月使用次数 */
    private String monthlyUsageFrequency;

    /** 需求文档地址 */
    private String requirementDocUrl;

    /** 系统 */
    private String systemModule;

    /** 需求部门 */
    private String requirementDept;

    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 需求类型 */
    private String requirementType;

    /** 需求指派人员 */
    private String assignPerson;

    /**多个需求指派人员*/
    @TableField(exist = false)
    private List<String> assignPersons;

    /**需求总耗工时*/
    @TableField(exist = false)
    private double totalTime;

    @TableField(exist = false)
    private List<MesRequirementTask> mesRequirementTaskList;

    /**部门*/
    private Long deptId;

    /**属性*/
    private String attribute;

    @TableField(exist = false)
    private List<Long> deptList;

    /**实际开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date actualBeginTime;

    /**实际结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date actualEndTime;

    /**交付状态*/
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String deliveryStatus;

    /**需求最早计划开始时间*/
    @TableField(exist = false)
    private Date minBeginTime;

    /**需求最晚计划结束时间*/
    @TableField(exist = false)
    private Date maxEndTime;

    /**需求最早实际开始时间*/
    @TableField(exist = false)
    private Date minActualBeginTime;

    /**需求最晚实际结束时间*/
    @TableField(exist = false)
    private Date maxActualEndTime;

    /** 项目名称 */
    @TableField(exist = false)
    private String projectName;

    /** 需求评审状态 */
    private String reviewStatus;

    /**需求收益类型*/
    @TableField
    private String incomeType;

    /**需求评审方式*/
    @TableField
    private String reviewMode;

    @TableField(exist = false)
    private boolean versionFlag;

    @TableField(exist = false)
    private List<String> requirementTypeList;

    @TableField(exist = false)
    private Date planBeginBeginTime;

    @TableField(exist = false)
    private Date planBeginEndTime;
}
