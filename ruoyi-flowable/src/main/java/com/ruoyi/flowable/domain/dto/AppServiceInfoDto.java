package com.ruoyi.flowable.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuji
 * @date 2023/1/2 10:46
 */
@NoArgsConstructor
@Data
public class AppServiceInfoDto {
    @JSONField(name = "app_service_id")
    private String appServiceId;
    @JSONField(name = "app_service_name")
    private String appServiceName;
}
