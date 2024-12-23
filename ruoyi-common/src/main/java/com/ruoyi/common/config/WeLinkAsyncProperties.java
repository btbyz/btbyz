package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "welink-async")
public class WeLinkAsyncProperties {

    private String address;
    private String sendRobotMsgUrl;
    private String sendCommonCardUrl;
    private String weLinkCommonCardPublicAccID;
    private String weLinkCommonCardUrlPath;
    private String weLinkRobotUrl;
    private String mesSchemaWeLinkRobotUrl;

}
