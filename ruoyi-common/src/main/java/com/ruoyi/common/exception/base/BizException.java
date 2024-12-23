package com.ruoyi.common.exception.base;

import com.ruoyi.common.enums.BaseErrorCodeEnum;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

    private Integer code;
    private String message;
    private String description;

    public BizException() {};

    public BizException(BaseErrorCodeEnum baseErrorCodeEnum) {
        super(baseErrorCodeEnum.getCode() + "-" + baseErrorCodeEnum.getMessage());
        this.code = baseErrorCodeEnum.getCode();
        this.message = baseErrorCodeEnum.getDescription();
        this.description = baseErrorCodeEnum.getDescription();

    }

    public BizException(BaseErrorCodeEnum baseErrorCodeEnum, String description){
        super(baseErrorCodeEnum.getCode() + "-" + description);
        this.code = baseErrorCodeEnum.getCode();
        this.message = baseErrorCodeEnum.getMessage();
        this.description = description;
    }
}
