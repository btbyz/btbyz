package com.ruoyi.flowable.service;

import com.ruoyi.flowable.domain.dto.MesProjectRecords;

import java.util.List;

/**
 * 需求项目记录Service接口
 * 
 * @author wangxin
 * @date 2024-03-11
 */
public interface IMesProjectRecordsService {

    /**
     * 查询需求项目记录
     * 
     * @param id 需求项目记录ID
     * @return 需求项目记录
     */
    MesProjectRecords selectMesProjectRecordsById(String id);

    /**
     * 查询需求项目记录列表
     * 
     * @param mesProjectRecords 需求项目记录
     * @return 需求项目记录集合
     */
    List<MesProjectRecords> selectMesProjectRecordsList(MesProjectRecords mesProjectRecords);

    /**
     * 新增需求项目记录
     * 
     * @param mesProjectRecords 需求项目记录
     * @return 结果
     */
    boolean insertMesProjectRecords(MesProjectRecords mesProjectRecords);

    /**
     * 修改需求项目记录
     * 
     * @param mesProjectRecords 需求项目记录
     * @return 结果
     */
    boolean updateMesProjectRecords(MesProjectRecords mesProjectRecords);

    /**
     * 批量删除需求项目记录
     * 
     * @param id 需要删除的需求项目记录ID
     * @return 结果
     */
    boolean deleteMesProjectRecordsByIds(String[] id);

    /**
     * 生成发布文档
     * @param id 项目ID
     * @return true/false
     * */
    boolean generateReleaseDocument(Long[] id);

}
