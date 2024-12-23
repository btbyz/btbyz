package com.ruoyi.flowable.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class WelinkSendDto {
    private List<String> UserIdList;
    private String msgContent;
    private String urlPath;
}
