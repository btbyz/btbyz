package com.ruoyi.flowable.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "calendar")
public class CalendarProperties {
    private String address;
    private String country;

    private String language;

    private String apiKey;
}
