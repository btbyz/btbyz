package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.flowable.common.enums.DeliveryStatusEnum;
import com.ruoyi.flowable.common.enums.RequirementStatusEnum;
import com.ruoyi.flowable.domain.dto.MesProject;
import com.ruoyi.flowable.mapper.MesProjectMapper;
import com.ruoyi.flowable.service.IMesOaRequirementService;
import com.ruoyi.flowable.service.IMesProjectService;
import com.ruoyi.flowable.util.RequirementUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-04-08
 */
@Service
public class MesProjectServiceImpl extends ServiceImpl<MesProjectMapper, MesProject> implements IMesProjectService {
    @Autowired
    private MesProjectMapper mesProjectMapper;
    @Autowired
    private IMesOaRequirementService mesOaRequirementService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IMesProjectService mesProjectService;

    private static final String PROJECT_CODE_PREFIX = "MITP";

    /**
     * 查询【请填写功能名称】
     *
     * @param projectCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesProject selectMesProjectByProjectCode(String projectCode)
    {
        return this.getById(projectCode);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesProject 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesProject> selectMesProjectList(MesProject mesProject)
    {
        return mesProjectMapper.selectMesProjectList(mesProject);
    }

    /**
     * 新增或修改【请填写功能名称】
     *
     * @param mesProject 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int saveOrUpdateMesProject(MesProject mesProject) {
        if (StringUtils.isBlank(mesProject.getProjectCode())) {
            // 新增
            FabAssertUtils.isNotBlank(mesProject.getProjectName(), BaseErrorCodeEnum.NULL_ERROR, "项目名称为空！");
            FabAssertUtils.isNotBlank(mesProject.getCreateUid(), BaseErrorCodeEnum.NULL_ERROR, "项目责任人为空！");
            MesProject project = new MesProject();
            project.setProjectName(mesProject.getProjectName());
            int count = mesProjectMapper.countByName(project);
            if (count > 0) {
                FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "项目名称已存在,请更改项目名称再提交!");
            }
            SysUser sysUser = sysUserService.selectUserByUserName(mesProject.getCreateUid());
            if (sysUser == null) {
                FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "未查询到责任人信息！");
            }
            mesProject.setCreateUname(sysUser.getNickName());
            MesProject mesProject1 = mesProjectMapper.selectOneByRequirementCodeDesc();
            String projectCode;
            String prefix = PROJECT_CODE_PREFIX;
            if (mesProject1 != null) {
                String yearCode = DateUtils.dateTimeNow("yyyy").substring(2);
                if (mesProject1.getProjectCode().contains(prefix + yearCode)) {
                    int num = Integer.parseInt(mesProject1.getProjectCode().substring(mesProject1.getProjectCode().length() - 5));
                    projectCode = RequirementUtils.getRequirementCode(prefix, num);
                } else {
                    projectCode = RequirementUtils.getRequirementCode(prefix, 0);
                }
            } else {
                projectCode = RequirementUtils.getRequirementCode(prefix, 0);
            }
            mesProject.setProjectCode(projectCode);
            // 设定交付状态
            mesProject.setDeliveryStatus(mesProjectService.getDeliveryStatus(mesProject));
            int num = mesProjectMapper.insert(mesProject);
            if (num > 0) {
                mesOaRequirementService.updateOaRequirementStatus(mesProject.getProjectOaId());
            }
            return num;
        } else {
            MesProject project = new MesProject();
            project.setProjectName(mesProject.getProjectName());
            project.setProjectCode(mesProject.getProjectCode());
            int count = mesProjectMapper.countByName(project);
            if (count > 0) {
                FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "项目名称已存在,请更改项目名称再提交!");
            }
            SysUser sysUser = sysUserService.selectUserByUserName(mesProject.getCreateUid());
            if (sysUser == null) {
                FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "未查询到责任人信息！");
            }
            mesProject.setCreateUname(sysUser.getNickName());
            SysUser loginUser = SecurityUtils.getLoginUser().getUser();
            if (loginUser != null) {
                mesProject.setUpdateUid(loginUser.getUserName());
                mesProject.setUpdateUname(loginUser.getNickName());
            }
            mesProject.setUpdateTime(new Date());
            // 设定交付状态
            mesProject.setDeliveryStatus(mesProjectService.getDeliveryStatus(mesProject));
            return mesProjectMapper.updateMesProjectById(mesProject);
        }
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectCodes 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public boolean deleteMesProjectByIds(List<String> projectCodes)
    {
        return this.removeByIds(projectCodes);
    }

    @Override
    public List<MesProject> getProjectCodeList() {
        QueryWrapper<MesProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MesProject::getStatus,"InProgress");
        return this.list(queryWrapper);
    }

    @Override
    public int
    updateProjectStatus(String projectCode) {
        MesProject project = this.getById(projectCode);
        if (project != null && project.getStatus().equals("UnStart")) {
            MesProject mesProject = new MesProject();
            mesProject.setProjectCode(projectCode);
            mesProject.setStatus("InProgress");
            return mesProjectMapper.updateById(mesProject);
        }
        return 0;
    }

    @Override
    public String getDeliveryStatus(MesProject mesProject) {
        String deliveryStatus = null;
        if (mesProject.getStatus().equals(RequirementStatusEnum.UN_START.getCode()) && mesProject.getBeginTime().before(DateUtils.parseDate(DateUtils.getDate()))) {
            deliveryStatus = DeliveryStatusEnum.NON_START.getCode();
        } else if (mesProject.getStatus().equals(RequirementStatusEnum.IN_PROGRESS.getCode())) {
            deliveryStatus = mesProject.getEndTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.ON_GOING.getCode();
        } else if (mesProject.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())) {
            deliveryStatus = mesProject.getEndTime().before(mesProject.getActualEndTime()) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.COMPLETED.getCode();
        }
        return deliveryStatus;
    }

}
