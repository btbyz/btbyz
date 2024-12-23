package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuji
 * @date 2022/1/20 8:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "org.quartz.scheduler")
public class ScheduleProperties {


    private boolean autoStartup;

}
