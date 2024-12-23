package com.ruoyi.common.utils;

import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.exception.base.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

public class FabAssertUtils {

    public static void isFalse(boolean flag, BaseErrorCodeEnum baseErrorCodeEnum) {
        if (flag) {
            throw new BizException(baseErrorCodeEnum);
        }
    }

    public static void isFalse(boolean flag, BaseErrorCodeEnum baseErrorCodeEnum, String description) {
        if (flag) {
            throw new BizException(baseErrorCodeEnum, description);
        }
    }

    public static void isTrue(boolean flag, BaseErrorCodeEnum baseErrorCodeEnum) {
        if (!flag) {
            throw new BizException(baseErrorCodeEnum);
        }
    }

    public static void isTrue(boolean flag, BaseErrorCodeEnum baseErrorCodeEnum, String description) {
        if (!flag) {
            throw new BizException(baseErrorCodeEnum, description);
        }
    }

    public static void isNotNull(Object obj, BaseErrorCodeEnum errorCodeEnum){
        if (obj == null) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isNotNull(Object obj){
        if (obj == null) {
            throw new BizException(BaseErrorCodeEnum.NULL_ERROR);
        }
    }
    public static void isNotNull(Object obj, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (obj == null) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isNull(Object obj, BaseErrorCodeEnum errorCodeEnum){
        if (obj != null) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isNull(Object obj, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (obj != null) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isEmpty(Collection<?> collection, BaseErrorCodeEnum errorCodeEnum){
        if (!isEmpty(collection)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isEmpty(Collection<?> collection, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (!isEmpty(collection)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isNotEmpty(Collection<?> collection, BaseErrorCodeEnum errorCodeEnum){
        if (isEmpty(collection)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isNotEmpty(Collection<?> collection, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (isEmpty(collection)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isNotEmpty(Collection<?> collection, String detailMessage){
        if (isEmpty(collection)) {
            throw new BizException(BaseErrorCodeEnum.PARAM_ERROR, detailMessage);
        }
    }

    public static void isNotEmpty(Collection<?> collection){
        if (isEmpty(collection)) {
            throw new BizException(BaseErrorCodeEnum.PARAM_ERROR);
        }
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static void isEmpty(Map<?, ?> map, BaseErrorCodeEnum errorCodeEnum){
        if (!isEmpty(map)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isEmpty(Map<?, ?> map, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (!isEmpty(map)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isNotEmpty(Map<?, ?> map, BaseErrorCodeEnum errorCodeEnum){
        if (isEmpty(map)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isNotEmpty(Map<?, ?> map, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (isEmpty(map)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static void isBlank(String str, BaseErrorCodeEnum errorCodeEnum){
        if (StringUtils.isNotBlank(str)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isBlank(String str, BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (StringUtils.isNotBlank(str)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }

    public static void isNotBlank(String str,  BaseErrorCodeEnum errorCodeEnum){
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCodeEnum);
        }
    }

    public static void isNotBlank(String str,  String  detailMessage){
        if (StringUtils.isBlank(str)) {
            throw new BizException(BaseErrorCodeEnum.NULL_ERROR, detailMessage);
        }
    }

    public static void isNotBlank(String str){
        if (StringUtils.isBlank(str)) {
            throw new BizException(BaseErrorCodeEnum.NULL_ERROR);
        }
    }

    public static void isNotBlank(String str,  BaseErrorCodeEnum errorCodeEnum, String detailMessage){
        if (StringUtils.isBlank(str)) {
            throw new BizException(errorCodeEnum, detailMessage);
        }
    }
}
