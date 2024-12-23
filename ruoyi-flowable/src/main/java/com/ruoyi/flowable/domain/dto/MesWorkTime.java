package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】对象 mes_work_time
 *
 * @author ruoyi
 * @date 2024-04-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MesWorkTime extends BaseEntityDto
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 填写日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    /** 工作周 */
    private String weekOfYear;

    /** 工时 */
    private String workHour;

    /** 编号(项目/需求/任务) */
    private String workCode;

    /** 工作类别 */
    private String workType;

    /** 业务部门 */
    private String businessDept;

    /** 时段类别 */
    private String periodType;

    /** 工作内容 */
    private String workDesc;

    /**填报人*/
    private String workUser;

    /**获取时间范围*/
    @TableField(exist = false)
    private Map<String, String> map;

    @TableField(exist = false)
    private Date dateNow;

    @TableField(exist = false)
    private Double totalHour;

    @TableField(exist = false)
    private String taskName;

    @TableField(exist = false)
    private Double dailyWorkHour;

    /** 一周工作理应总时长 */
    @TableField(exist = false)
    private Double weeklyWorkHour;

    /** 一周利用工作日期 */
    @TableField(exist = false)
    private List<String> weeklyWorkDateList;

    @TableField(exist = false)
    private Double monday;

    @TableField(exist = false)
    private Double tuesday;

    @TableField(exist = false)
    private Double wednesday;

    @TableField(exist = false)
    private Double thursday;

    @TableField(exist = false)
    private Double friday;

    @TableField(exist = false)
    private Double saturday;

    @TableField(exist = false)
    private Double sunday;

    /** 优化类占比 */
    @TableField(exist = false)
    private String optimizationProportion;

    /** 项目类占比 */
    @TableField(exist = false)
    private String projectProportion;

    /** 运维类占比 */
    @TableField(exist = false)
    private String operationProportion;

    /** 管理类占比 */
    @TableField(exist = false)
    private String managementProportion;

    /** 学习类占比 */
    @TableField(exist = false)
    private String studyProportion;

    /** 查询总工时分组信息 */
    @TableField(exist = false)
    private List<String> groupFieldList;

    /** 最新任务最新一条工时管理记录的填写日期 */
    @TableField(exist = false)
    private Date maxWorkTime;
}