package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesRequirementTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-04-10
 */
public interface MesRequirementTaskMapper extends BaseMapper<MesRequirementTask> {

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
    public List<MesRequirementTask> selectMesRequirementTaskListForTimeCard(MesRequirementTask mesRequirementTask);

    public MesRequirementTask selectOneByRequirementCodeDesc();
    public int countByName(MesRequirementTask mesRequirementTask);

    List<MesRequirementTask> getTaskCodes(@Param("deptId") Long deptId);

    public void deleteByRequirementCode(@Param("requirementCodes") List<String> requirementCodes);

    public MesRequirementTask selectTimeEndpoint(MesRequirementTask mesRequirementTask);

}
