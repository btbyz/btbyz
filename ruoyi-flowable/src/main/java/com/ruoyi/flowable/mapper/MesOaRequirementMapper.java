package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesOaRequirement;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-05-22
 */
public interface MesOaRequirementMapper extends BaseMapper<MesOaRequirement> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesOaRequirement 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesOaRequirement> selectMesOaRequirementList(MesOaRequirement mesOaRequirement);

}
