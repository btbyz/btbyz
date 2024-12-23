package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【请填写功能名称】对象 mes_project
 *
 * @author ruoyi
 * @date 2024-04-08
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MesProject extends BaseEntityDto {
    private static final long serialVersionUID = 1L;

    /** 项目编号 */
    @TableId
    private String projectCode;

    /** 项目名称 */
    private String projectName;

    /** 项目描述 */
    private String projectDesc;

    /** oa申请单号 */
    private String projectOaId;

    /** 项目状态。UnStart:未开始、InProgress:进行中、Completed:已完成、Delete:已删除、Cancel:取消 */
    private String status;

    /** 发布版本 */
    private String releaseVersion;

    /** 项目用户 */
    private String projectUser;

    /** 收益 */
    private String income;

    /** 项目文档地址 */
    private String projectDocUrl;

    /** 系统 */
    private String systemModule;

    /** 项目部门 */
    private String projectDept;

    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 项目指派人员 */
    private String assignPerson;

    /** 权限标识 */
    private String permissionFlag;

    /** 部门 */
    private Long deptId;

    /** 项目所属范围 */
    private String factoryCode;

    /** 实际开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualBeginTime;

    /** 实际结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualEndTime;

    /**交付状态*/
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String deliveryStatus;
}