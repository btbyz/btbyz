package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.ruoyi.common.enums.FabConstants.*;

/**
 * @author yuji
 * @date 2022/1/21 9:38
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final Logger log = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ... ");

        if (metaObject.hasSetter(CREATE_UID) || metaObject.hasSetter(CREATE_UNAME) || metaObject.hasSetter(UPDATE_UID) || metaObject.hasSetter(UPDATE_UNAME)) {
            try {
                if (SecurityUtils.getLoginUser() != null) {

                    SysUser user = SecurityUtils.getLoginUser().getUser();
                    if (this.getFieldValByName(CREATE_UID, metaObject) == null) {
                        this.setFieldValByName(CREATE_UID, user.getUserName(), metaObject);
                    }
                    if (this.getFieldValByName(CREATE_UNAME, metaObject) == null) {
                        this.setFieldValByName(CREATE_UNAME, user.getNickName(), metaObject);
                    }
                    if (this.getFieldValByName(UPDATE_UID, metaObject) == null) {
                        this.setFieldValByName(UPDATE_UID, user.getUserName(), metaObject);
                    }
                    if (this.getFieldValByName(UPDATE_UNAME, metaObject) == null) {
                        this.setFieldValByName(UPDATE_UNAME, user.getNickName(), metaObject);
                    }
                }
            } catch (CustomException e) {
                log.error("insert fill error. message:{}", e.getMessage());
            } catch (Exception e) {
                log.error("insert fill error", e);
            }
        }
        if (this.getFieldValByName(CREATE_TIME, metaObject) == null) {
            this.setFieldValByName(CREATE_TIME, new Date(), metaObject);
        }
        if (this.getFieldValByName(UPDATE_TIME, metaObject) == null) {
            this.setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ... ");
        if (metaObject.hasSetter(UPDATE_UID) || metaObject.hasSetter(UPDATE_UNAME)) {
            try {
                if (SecurityUtils.getLoginUser() != null) {
                    SysUser user = SecurityUtils.getLoginUser().getUser();
                    this.setFieldValByName(UPDATE_UID, user.getUserName(), metaObject);
                    this.setFieldValByName(UPDATE_UNAME, user.getNickName(), metaObject);
                }
            } catch (CustomException e) {
                log.error("update fill error. message:{}", e.getMessage());
            } catch (Exception e) {
                log.error("update fill error", e);
            }
        }
        this.setFieldValByName(UPDATE_TIME, new Date() , metaObject);
    }
}

