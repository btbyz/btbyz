package com.ruoyi.flowable.mapper;

import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.domain.dto.MesProjectRecords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 需求项目记录Mapper接口
 *
 * @author wangxin
 * @date 2024-03-11
 */
public interface MesProcessReleaseLogMapper
{
    MesProcessReleaseLog selectMesProcessReleaseLogById(Long id);
    /**
     * 查询需求项目记录列表
     *
     * @param mesProcessReleaseLog 需求项目记录
     * @return 需求项目记录集合
     */
    List<MesProcessReleaseLog> selectMesProcessReleaseLogList(MesProcessReleaseLog mesProcessReleaseLog);
    boolean insertMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog);
    boolean updateMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog);
    boolean deleteMesProcessReleaseLog(Long[] id);

    List<String> getVersion();

    Long getCount(@Param("year") long year, @Param("month") long month);
}

