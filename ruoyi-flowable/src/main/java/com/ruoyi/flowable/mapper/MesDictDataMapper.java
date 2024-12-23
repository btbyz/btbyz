package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MesDictDataMapper extends BaseMapper<MesDictData>{
    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesDictData 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<MesDictData> selectMesDictDataList(MesDictData mesDictData);

    int count(@Param("dictCode")String dictCode, @Param("module")String module);
}
