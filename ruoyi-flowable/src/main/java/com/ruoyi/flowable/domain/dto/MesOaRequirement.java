package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 【请填写功能名称】对象 mes_oa_requirement
 *
 * @author ruoyi
 * @date 2024-05-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MesOaRequirement implements Serializable {
    private static final long serialVersionUID = 1L;

    /** OA单号 */
    @TableId
    private String requirementOaId;

    /** 需求名称 */
    private String requirementName;

    /** 事件&服务请求描述 */
    private String requirementDesc;

    /** 服务目录 */
    private String systemModule;

    /** MES服务运维申请人 */
    private String requirementUser;

    /** 申请人所属部门 */
    private String requirementDept;

    /** 附件文件名 */
    private String fileName;

    /** 文件地址 */
    private String filePath;

    /** 需求/运维类型 */
    private String requirementType;

    /** 需求等级 */
    private String requirementGrade;

    /** 需求收益 */
    private String income;

    /** 业务归口部门 */
    private String businessDept;

    /** 处理责任人 */
    private String responsibleUname;

    /** 处理责任人工号 */
    private String responsibleUid;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createUname;

    /** 创建者工号 */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private String createUid;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUname;

    /** 更新者工号 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUid;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String status;

    @TableField(exist = false)
    private String createDate;

    @TableField(exist = false)
    private byte[] fileBytes;


}