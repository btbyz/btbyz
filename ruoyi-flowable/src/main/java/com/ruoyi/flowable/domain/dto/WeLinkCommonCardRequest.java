package com.ruoyi.flowable.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yuji
 * @date 2023/1/11 10:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeLinkCommonCardRequest {

    private String msgOwner;
    private String publicAccID;
    private String msgRange;
    private List<String> toUserList;
    private String msgTitle;
    private String msgContent;
    private String receiveDeviceType;
    private String urlType;
    private String urlPath;
    private String desktopUrlPath;
    private String statusColor;
    private String isForceTips;
    private String clientId;
    private String clientSecret;

}
