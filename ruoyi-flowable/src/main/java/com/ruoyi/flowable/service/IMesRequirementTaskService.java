package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesRequirementTask;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-04-10
 */
public interface IMesRequirementTaskService extends IService<MesRequirementTask> {
    /**
     * 查询【请填写功能名称】
     *
     * @param taskCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesRequirementTask selectMesRequirementTaskByTaskCode(String taskCode);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesRequirementTask> selectMesRequirementTaskList(MesRequirementTask mesRequirementTask);

    /**
     * 查询timecard可用的任务列表
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<MesRequirementTask> selectMesRequirementTaskListForTimeCard(MesRequirementTask mesRequirementTask);

    /**
     * 新增【请填写功能名称】
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 结果
     */
    public int insertMesRequirementTask(MesRequirementTask mesRequirementTask);

    /**
     * 指派任务
     *
     * @param mesRequirementTask 指派任务对象
     * @return 结果
     */
    public int assignTask(MesRequirementTask mesRequirementTask);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 结果
     */
    public int updateMesRequirementTask(MesRequirementTask mesRequirementTask);

    /**
     * 修改【请填写功能名称】
     *
     * @param requirementCodes 需求编号集合
     * @param status 需求状态
     * @return 结果
     */
    boolean updateBatchMesRequirementTaskStatus(List<String> requirementCodes, String status);

    /**
     * 激活任务列表，允许修改任务开始时间和结束时间
     *
     * @param requirementCodes 需求编号集合
     * @param status 需求状态
     * @return 结果
     */
    boolean updateBatchMesRequirementTaskStatus(List<String> requirementCodes, String status, Date beginTime, Date endTime);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param taskCodes 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteMesRequirementTaskByTaskCodes(List<String> taskCodes);

    /**
     * 根据需求code批量删除任务列表
     *
     * @param requirementCodes 需要删除的需求code
     */
    public void deleteMesRequirementTaskByRequirementCode(List<String> requirementCodes);

    /**
     * 获取任务编号
     * */
    List<MesRequirementTask> getTaskCodes(Long systemModule);

    /**
     * 查找需求下任务的最小计划开始时间和最大计划结束时间
     */
    MesRequirementTask selectTimeEndpoint(MesRequirementTask mesRequirementTask);

    /**
     * 任务改变引起需求状态变动
     * @param mesRequirementTask
     */
    void changeRequirementStatus(MesRequirementTask mesRequirementTask);

    /**
     * 获取交付状态
     * @param mesRequirementTask
     * @return
     */
    String getDeliveryStatus(MesRequirementTask mesRequirementTask);
}