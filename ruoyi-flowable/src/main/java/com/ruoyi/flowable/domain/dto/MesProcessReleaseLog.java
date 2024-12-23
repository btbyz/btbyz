package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 流程发布日志 mes_process_release_log
 *
 * @author zhangtao
 * @date 2024-03-25
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MesProcessReleaseLog extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String version;

    private String releaseDimension;

    private String releaseContent;

    private String status;

    private Long year;

    private Long month;

    private String createUid;

    private String createUname;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    private String updateUid;

    private String updateUname;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private List<String> versionList;

}
