package com.ruoyi.flowable.common.enums;

public enum RequirementStatusEnum {

    /**
     * 需求状态码
     * */
    UN_START("UnStart", "未开始"),
    IN_PROGRESS("InProgress", "进行中"),
    COMPLETED("Completed", "已完成"),
    DELETE("Delete", "已删除");

    private final String code;

    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    RequirementStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
