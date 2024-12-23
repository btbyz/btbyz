package com.ruoyi.flowable.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

import java.util.List;

public class RequirementUtils {

    /**
     * 生成需求编号
     * 例：MITE  24   00075
     *
     * @param number 现有需求个数
     * @return 需求编号
     * */
    public static String getRequirementCode(String prefix, int number) {
        String year = DateUtils.dateTimeNow("yyyy");
        if (StringUtils.isNotBlank(year)) {
            String yearCode = year.substring(2);
            return prefix + yearCode + StringUtils.leftPad(String.valueOf(number + 1), 5, '0');
        }
        return null;
    }

    public static String getUserRole() {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser != null && CollectionUtils.isNotEmpty(sysUser.getRoles())) {
            List<SysRole> sysRoles = sysUser.getRoles();
            String role = null;
            for (SysRole sysRole : sysRoles) {
                if (sysRole.getRoleId() == 1) {
                    role = "admin";
                    break;
                } else if (sysRole.getRoleId() == 2){
                    role = "flow";
                }
            }
            return role;
        }
        return null;
    }
}
