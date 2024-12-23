package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesProject;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-04-08
 */
public interface MesProjectMapper extends BaseMapper<MesProject> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesProject 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesProject> selectMesProjectList(MesProject mesProject);

    int countByName(MesProject mesProject);

    int updateMesProjectById(MesProject mesProject);

    MesProject selectOneByRequirementCodeDesc();
}