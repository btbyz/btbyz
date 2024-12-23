package com.ruoyi.flowable.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuji
 * @date 2023/2/8 15:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameValueDto {

    private String name;
    private String value;
    private int seq;

}
