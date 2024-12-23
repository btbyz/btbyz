package com.ruoyi.flowable.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesOaRequirement;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-05-22
 */
public interface IMesOaRequirementService extends IService<MesOaRequirement> {
    /**
     * 查询【请填写功能名称】
     *
     * @param requirementOaId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesOaRequirement selectMesOaRequirementByRequirementOaId(String requirementOaId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesOaRequirement> selectMesOaRequirementList(MesOaRequirement mesOaRequirement);

    /**
     * 新增【请填写功能名称】
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 结果
     */
    public void insertMesOaRequirement(MesOaRequirement mesOaRequirement);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 结果
     */
    public int updateMesOaRequirement(MesOaRequirement mesOaRequirement);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param requirementOaIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteMesOaRequirementByRequirementOaIds(List<String> requirementOaIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param requirementOaId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteMesOaRequirementByRequirementOaId(String requirementOaId);

    List<MesOaRequirement> getAllRequirement(MesOaRequirement mesOaRequirement);

    /**
     * 变更OA需求单状态
     *
     *
     * @author zhangtao
     * @param requirementOaId OA单号
     * @date 2024-06-03 09:46:00
     * */
    void updateOaRequirementStatus(String requirementOaId);

}

