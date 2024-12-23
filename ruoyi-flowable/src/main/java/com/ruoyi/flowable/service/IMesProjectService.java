package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesProject;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-04-08
 */
public interface IMesProjectService extends IService<MesProject> {
    /**
     * 查询【请填写功能名称】
     *
     * @param projectCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesProject selectMesProjectByProjectCode(String projectCode);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesProject 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesProject> selectMesProjectList(MesProject mesProject);

    /**
     * 新增或修改【请填写功能名称】
     *
     * @param mesProject 【请填写功能名称】
     * @return 结果
     */
    public int saveOrUpdateMesProject(MesProject mesProject);


    /**
     * 批量删除【请填写功能名称】
     *
     * @param projectCodes 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public boolean deleteMesProjectByIds(List<String> projectCodes);

    List<MesProject> getProjectCodeList();

    int updateProjectStatus(String projectCode);

    /**
     * 获取交付状态
     * @param mesProject
     * @return
     */
    String getDeliveryStatus(MesProject mesProject);
}
