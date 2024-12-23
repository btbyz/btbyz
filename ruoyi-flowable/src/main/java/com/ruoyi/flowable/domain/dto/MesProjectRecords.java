package com.ruoyi.flowable.domain.dto;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 需求项目记录对象 mes_project_records
 * 
 * @author wangxin
 * @date 2024-03-11
 */
@Data
public class MesProjectRecords extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String factoryArea;

    private String module;

    private String project;

    private String developers;

    private String user;

    private String importantLevel;

    private String urgencyLevel;

    private String income;

    private String monthlyUsageFrequency;

    private String personalTesting;

    private String oaNumber;

    private String date;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String updateBy;

    private String state;

    private String version;

}
