package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesRequirement;
import com.ruoyi.flowable.domain.vo.MesRequirementVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IMesRequirementService extends IService<MesRequirement> {

    /**
     * 查询【请填写功能名称】
     *
     * @param requirementCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesRequirement selectMesRequirementByRequirementCode(String requirementCode);


    /**
     * 获取需求明细数据 包括需求明细+任务明细数据
     * */
    public MesRequirement getRequirementsDetails(MesRequirement mesRequirement);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesRequirement> selectMesRequirementList(MesRequirement mesRequirement);

    List<MesRequirement> getRequirementList(MesRequirement mesRequirement);

    /**
     * 新增需求
     *
     * @param mesRequirement 需求对象
     * @return 影响行数
     */
    public int insertMesRequirement(MesRequirement mesRequirement);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 结果
     */
    public int updateMesRequirement(MesRequirement mesRequirement);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesRequirements 【请填写功能名称】
     * @return 结果
     */
    public boolean updateBatchMesRequirementById(List<MesRequirement> mesRequirements);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param requirementCodes 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public boolean deleteMesRequirementByRequirementCodes(List<String> requirementCodes);

    /**
     * 取消当前需求和当前需求下的所有任务
     *
     * @param mesRequirement 需要取消的需求
     * @return 结果
     */
    boolean cancelMesRequirementByRequirementCode(MesRequirement mesRequirement);

    /**
     * 修改当前需求和当前需求下的所有任务
     *
     * @param mesRequirement 需要修改的需求
     * @return 结果
     */
    boolean updateRequirementAndTask(MesRequirement mesRequirement);

    /**
     * 获取需求编号
     * @param mesRequirement
     * */
    List<MesRequirement> getRequirementCodes(MesRequirement mesRequirement);

    /**
     * 通过需求编号获取任务、需求详细信息
     * */
    List<MesRequirementVo> generateReleaseDocument(List<String> id);

    List<MesRequirementVo> getReleaseDocument(List<String> ids);

    /**
     * 通过登录账号获取工时管理需要的需求
     * */
    List<MesRequirement> getRequirementsByUserName();


    /**
     * 获取交付状态
     * @param mesRequirement
     * @return
     */
    String getDeliveryStatus(MesRequirement mesRequirement);

    /**
     * @param mesRequirement
     * 查找项目下需求的最小计划开始时间和最大计划结束时间
     */
    MesRequirement selectTimeEndpointForProject(MesRequirement mesRequirement);

    /**
     * 需求改变引起项目变动
     * @param mesRequirement
     */
    void changeProjectStatus(MesRequirement mesRequirement);

    /**
     * 文件导出
     *
     * @param mesRequirement
     * @param response
     */
    void export(MesRequirement mesRequirement, HttpServletResponse response);
}

