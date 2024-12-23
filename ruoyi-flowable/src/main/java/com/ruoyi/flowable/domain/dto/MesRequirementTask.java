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
 * 【请填写功能名称】对象 mes_requirement_tasks
 *
 * @author ruoyi
 * @date 2024-04-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MesRequirementTask extends BaseEntityDto
{
    private static final long serialVersionUID = 1L;

    /** 任务编号 */
    @TableId
    private String taskCode;

    /** 任务名称 */
    private String taskName;

    /** 需求编号 */
    private String requirementCode;

    /** 开发人员 */
    private String developers;

    /** 任务开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 任务结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 任务状态 */
    private String status;

    /** 任务类型 */
    private String taskType;

    /**指派人员*/
    @TableField(exist = false)
    private String assignPerson;

    /**部门*/
    private Long deptId;

    /**发版本*/
    private String releaseVersion;

    @TableField(exist = false)
    private MesRequirement requirement;

    /**任务消耗工时*/
    @TableField(exist = false)
    private double taskTime;

    /** 需求名称 */
    @TableField(exist = false)
    private String requirementName;

    /** 需求属性 */
    @TableField(exist = false)
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

    /**任务最早计划开始时间*/
    @TableField(exist = false)
    private Date minBeginTime;

    /**任务最晚计划结束时间*/
    @TableField(exist = false)
    private Date maxEndTime;

    /**任务最早实际开始时间*/
    @TableField(exist = false)
    private Date minActualBeginTime;

    /**任务最晚实际结束时间*/
    @TableField(exist = false)
    private Date maxActualEndTime;

}
