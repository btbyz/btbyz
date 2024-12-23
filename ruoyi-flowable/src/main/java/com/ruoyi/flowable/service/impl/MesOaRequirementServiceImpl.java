package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.flowable.domain.dto.MesOaRequirement;
import com.ruoyi.flowable.mapper.MesOaRequirementMapper;
import com.ruoyi.flowable.service.IMesOaRequirementService;
import com.ruoyi.framework.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-05-22
 */
@Service
public class MesOaRequirementServiceImpl extends ServiceImpl<MesOaRequirementMapper, MesOaRequirement> implements IMesOaRequirementService {

    private static final Logger log = LoggerFactory.getLogger(MesOaRequirementServiceImpl.class);

    @Autowired
    private MesOaRequirementMapper mesOaRequirementMapper;
    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询【请填写功能名称】
     *
     * @param requirementOaId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesOaRequirement selectMesOaRequirementByRequirementOaId(String requirementOaId) {
        return mesOaRequirementMapper.selectById(requirementOaId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesOaRequirement> selectMesOaRequirementList(MesOaRequirement mesOaRequirement) {
        return mesOaRequirementMapper.selectMesOaRequirementList(mesOaRequirement);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public void insertMesOaRequirement(MesOaRequirement mesOaRequirement) {
        FabAssertUtils.isNotBlank(mesOaRequirement.getRequirementOaId(), BaseErrorCodeEnum.NULL_ERROR, "OA单号为空");
        FabAssertUtils.isNotBlank(mesOaRequirement.getCreateDate(), BaseErrorCodeEnum.NULL_ERROR, "创建时间为空");
        Date createTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, mesOaRequirement.getCreateDate());
        mesOaRequirement.setCreateTime(createTime);
        mesOaRequirement.setStatus("未指派");
        if (StringUtils.isNotBlank(mesOaRequirement.getFileName()) && StringUtils.isNotNull(mesOaRequirement.getFileBytes())) {
            String[] nameArr =  mesOaRequirement.getFileName().split("\\.");
            if (nameArr.length < 2) {
                return;
            }
            String fileName = nameArr[0] + "_" + System.currentTimeMillis() + "." + nameArr[1];
            FileUtils.bytesToFile(mesOaRequirement.getFileBytes(), RuoYiConfig.getProfile(), fileName);
            String filePath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/" + fileName;
            mesOaRequirement.setFilePath(filePath);
        }
        mesOaRequirementMapper.insert(mesOaRequirement);
        mesOaRequirement.setFileBytes(null);
    }


    /**
     * 修改【请填写功能名称】
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMesOaRequirement(MesOaRequirement mesOaRequirement) {
        return mesOaRequirementMapper.updateById(mesOaRequirement);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param requirementOaIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMesOaRequirementByRequirementOaIds(List<String> requirementOaIds) {
        return mesOaRequirementMapper.deleteBatchIds(requirementOaIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param requirementOaId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMesOaRequirementByRequirementOaId(String requirementOaId) {
        return mesOaRequirementMapper.deleteById(requirementOaId);
    }

    @Override
    public List<MesOaRequirement> getAllRequirement(MesOaRequirement mesOaRequirement) {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (StringUtils.isNotNull(sysUser)) {
            mesOaRequirement.setCreateUid(sysUser.getUserName());
            mesOaRequirement.setStatus("未指派");
            return mesOaRequirementMapper.selectMesOaRequirementList(mesOaRequirement);
        }
        return new ArrayList<>();
    }

    @Override
    public void updateOaRequirementStatus(String requirementOaId) {
        MesOaRequirement mesOaRequirement = new MesOaRequirement();
        mesOaRequirement.setRequirementOaId(requirementOaId);
        mesOaRequirement.setStatus("已指派");
        mesOaRequirementMapper.updateById(mesOaRequirement);

    }

}