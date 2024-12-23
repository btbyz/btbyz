package com.ruoyi.flowable.mapper;

import java.util.List;

import com.ruoyi.flowable.domain.dto.MesProjectRecords;

/**
 * 需求项目记录Mapper接口
 * 
 * @author wangxin
 * @date 2024-03-11
 */
public interface MesProjectRecordsMapper
{
     MesProjectRecords selectMesProjectRecordsById(String id);
    /**
     * 查询需求项目记录列表
     * 
     * @param mesProjectRecords 需求项目记录
     * @return 需求项目记录集合
     */
    List<MesProjectRecords> selectMesProjectRecordsList(MesProjectRecords mesProjectRecords);
    boolean insertMesProjectRecords(MesProjectRecords mesProjectRecords);
    boolean updateMesProjectRecords(MesProjectRecords mesProjectRecords);
    boolean deleteMesProjectRecords(String[] id);

    List<MesProjectRecords> getMesProjectRecordsList(Long[] id);

}
