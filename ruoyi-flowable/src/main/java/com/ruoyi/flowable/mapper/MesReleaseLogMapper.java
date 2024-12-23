package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesReleaseLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-04-15
 */
public interface MesReleaseLogMapper extends BaseMapper<MesReleaseLog> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesReleaseLog> selectMesReleaseLogList(MesReleaseLog mesReleaseLog);

    int getCount(@Param("year") Long year, @Param("month") Long month);


}
