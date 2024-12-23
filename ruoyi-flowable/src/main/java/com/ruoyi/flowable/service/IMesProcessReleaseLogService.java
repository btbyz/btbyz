package com.ruoyi.flowable.service;

import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;

import java.util.List;

/**
 * 流程发布日志 mes_process_release_log
 *
 * @author zhangtao
 * @date 2024-03-25
 */
public interface IMesProcessReleaseLogService {

    /**
     * 查询流程发布日志
     *
     * @param id 流程发布日志ID
     * @return 流程发布日志
     */
    MesProcessReleaseLog selectMesProcessReleaseLogById(Long id);

    /**
     * 查询流程发布日志列表
     *
     * @param mesProcessReleaseLog 流程发布日志
     * @return 流程发布日志集合
     */
    List<MesProcessReleaseLog> selectMesProcessReleaseLogList(MesProcessReleaseLog mesProcessReleaseLog);

    /**
     * 新增流程发布日志
     *
     * @param mesProcessReleaseLog 流程发布日志
     * @return 结果
     */
    boolean insertMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog);

    /**
     * 修改流程发布日志
     *
     * @param mesProcessReleaseLog 流程发布日志
     * @return 结果
     */
    boolean updateMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog);

    /**
     * 批量删除流程发布日志
     *
     * @param id 需要删除的流程发布日志ID
     * @return 结果
     */
    boolean deleteMesProcessReleaseLogByIds(Long[] id);

    List<String> getVersion();

}

