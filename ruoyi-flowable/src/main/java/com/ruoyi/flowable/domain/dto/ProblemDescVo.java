package com.ruoyi.flowable.domain.dto;

import com.ruoyi.flowable.config.annotation.ExcelConverterField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemDescVo {
    @ExcelConverterField("时间")
    private String time;
    @ExcelConverterField("时长")
    private String duration;
    @ExcelConverterField("现象")
    private String details;
    @ExcelConverterField("范围")
    private String scope;
}

