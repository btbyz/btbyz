package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.*;

import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MesDictData extends BaseEntityDto {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String module;

    private String dictCode;

    private String dictValue;

    private Long dictSeq;

    private String status;

    private String remark;

    @TableField(exist = false)
    private Set<String> modules;

    @TableField(exist = false)
    private Set<String> dictCodes;

    @TableField(exist = false)
    private Set<String> remarks;

}
