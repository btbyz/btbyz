package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesRequirement;
import com.ruoyi.flowable.domain.vo.MesRequirementVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-04-08
 */
public interface MesRequirementMapper extends BaseMapper<MesRequirement> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesRequirement> selectMesRequirementList(MesRequirement mesRequirement);

    public MesRequirement selectOneByRequirementCodeDesc(MesRequirement mesRequirement);

    public int updateMesRequirement(MesRequirement mesRequirement);
    public int countByName(MesRequirement mesRequirement);

    List<MesRequirement> getRequirementCodes(MesRequirement mesRequirement);

    List<MesRequirementVo> generateReleaseDocument(@Param("ids") List<String> id);

    List<MesRequirement> getRequirementsByUserName(MesRequirement mesRequirement);

    public MesRequirement selectTimeEndpointForProject(MesRequirement mesRequirement);

}
