package com.ruoyi.flowable.common.enums;

public enum DeliveryStatusEnum {

    /**
     * 需求状态码
     * */
    NON_START("Non-Start", "未准时开始"),
    OVER_DUE("Over-Due", "延期交付"),
    COMPLETED("Completed", "按时交付"),
    ON_GOING("On-Going", "进行中");

    private final String code;

    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    DeliveryStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
