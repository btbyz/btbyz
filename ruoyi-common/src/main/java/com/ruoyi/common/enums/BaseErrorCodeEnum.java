package com.ruoyi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础异常信息错误代码枚举
 * */
@Getter
@AllArgsConstructor
public enum BaseErrorCodeEnum {

    /**
     * 错误m码定义
     * */
    SUCCESS_CODE (0, "success", "成功"),
    SUCCESS (200, "success", "成功"),
    UNKNOWN (9999, "unknown error", "未知异常"),
    PARAM_ERROR(1001, "param_error", "参数错误"),
    NULL_ERROR(1001, "null_error", "空指针错误"),
    ;

    private final Integer code;
    private final String message;
    private final String description;

}
