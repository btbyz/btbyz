package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 【请填写功能名称】对象 mes_release_log
 *
 * @author ruoyi
 * @date 2024-04-15
 */

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class MesReleaseLog extends BaseEntityDto
{
    private static final long serialVersionUID = 1L;

    /**时间唯一标识*/
    @TableId
    private Long timeKey;

    /** 版本号 */
    private String releaseVersion;

    /** 申请日期 */
    private Date applyDate;

    /** 申请人姓名 */
    private String applyUname;

    /** 申请人工号 */
    private String applyUid;

    /** 发布状态。InProgress:进行中，Completed:已完成、Rejected:已驳回 */
    private String status;

    /** 发布内容描述html */
    private String releaseDescInfo;

    /** 项目负责人 */
    private String projectLeader;

    /** 项目负责人审批标记 */
    private String projectLeaderApprovalMark;

    /** 项目负责人审批时间 */
    private Date projectLeaderApprovalTime;

    /** 项目负责人审批备注 */
    private String projectLeaderApprovalRemark;

    /** 产品负责人 */
    private String productLeader;

    /** 产品负责人审批标记 */
    private String productLeaderApprovalMark;

    /** 产品负责人审批时间 */
    private Date productLeaderApprovalTime;

    /** 产品负责人审批备注 */
    private String productLeaderApprovalRemark;

    /** 部门经理 */
    private String deptLeader;

    /** 部门经理审批标记 */
    private String deptLeaderApprovalMark;

    /** 部门经理审批时间 */
    private Date deptLeaderApprovalTime;

    /** 部门经理审批备注 */
    private String deptLeaderApprovalRemark;

    /** 发布完成时间 */
    private Date finishTime;

    /** 年份 */
    private Integer year;

    /** 月份 */
    private Integer month;

}
