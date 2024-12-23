package com.ruoyi.flowable.domain.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yuji
 * @date 2023/1/2 10:45
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeLinkSendRequest {
    /**
     * 聊天消息发送后台应用基础信息，用户客户端界面呈现，服务端不做解析。
     * {
     * "app_service_id": "",
     * "app_service_name": ""
     * }
     * app_service_id：第三方应用标识
     * app_service_name：第三方应用名称。
     */
    @JSONField(name = "app_service_info")
    private AppServiceInfoDto appServiceInfo;
    /**
     * 消息标识，请求中由消息发送端输入（全局唯一，建议使用UUID）。
     */
    @JSONField(name = "app_msg_id")
    private String appMsgId;
    /**
     * 消息接收群ID列表
     */
    @JSONField(name = "group_id")
    private List<String> groupId;
    /**
     * 消息内容类型:
     * 0—表示纯文本
     * 10—卡片消息
     */
    @JSONField(name = "content_type")
    private Integer contentType;
    /**
     * 消息具体内容
     */
    @JSONField(name = "content")
    private String content;
    /**
     * 消息应用归属标识，固定为1。
     */
    @JSONField(name = "client_app_id")
    private String clientAppId;
    /**
     * 此消息是否需要离线推送，默认false。
     */
    @JSONField(name = "is_push")
    private Boolean isPush;
    /**
     * 离线push推送附加信息，可为空。如果为空，则不作离线push推送的显示加强。{“pushTitle”:””;“pushDetail”:””}
     */
    @JSONField(name = "push_ext_data")
    private String pushExtData;
    /**
     * 客户端消息发送时间，可用于消息链路跟踪审计；
     */
    @JSONField(name = "client_send_time")
    private Long clientSendTime;
    /**
     * 值为字符串，示例：{"senderDisplay": "1"}。senderDisplay为1时客户端发送者名称显示为app_service_info.app_service_name；为0时客户端显示为系统账号名称。未传时，客户端按照senderDisplay为0处理
     */
    @JSONField(name = "msg_ext")
    private String msgExt;

    @JSONField(serialize = false)
    private byte[] fileBytes;

    @JSONField(serialize = false)
    private String fileName;

    private String clientId;
    private String clientSecret;
    private List<NameValueDto> messageBody;

    private String robotUrl;
    private Boolean atAllFlag;
    private Boolean isAtFlag;
    private List<String> atUserList;

    private CardInfo cardInfo;

    @Data
    public static class CardInfo{
        private String sourceUrl;
        private String digest;
        private String title;
    }


}
