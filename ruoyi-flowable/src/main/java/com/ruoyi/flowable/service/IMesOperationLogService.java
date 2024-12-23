package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesOperationLog;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 系统运维日志Service接口
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
public interface IMesOperationLogService  extends IService<MesOperationLog> {
    /**
     * 查询系统运维日志
     * 
     * @param id 系统运维日志主键
     * @return 系统运维日志
     */
    public MesOperationLog selectMesOperationLogById(Long id);

    /**
     * 查询系统运维日志列表
     * 
     * @param mesOperationLog 系统运维日志
     * @return 系统运维日志集合
     */
    public List<MesOperationLog> selectMesOperationLogList(MesOperationLog mesOperationLog);

    /**
     * 新增系统运维日志
     *
     * @param mesOperationLog 系统运维日志
     * @return 结果
     */
    public boolean insertMesOperationLog(MesOperationLog mesOperationLog);

    /**
     * 修改系统运维日志
     *
     * @param mesOperationLog 系统运维日志
     * @return
     */
    public boolean updateMesOperationLog(MesOperationLog mesOperationLog);

    /**
     * 批量删除系统运维日志
     * 
     * @param ids 需要删除的系统运维日志主键集合
     * @return 结果
     */
    public int deleteMesOperationLogByIds(Long[] ids);


    /**
     *批量新增运维日志
     *
     * @param mesOperationLogs 系统运维日志
     * @return 结果
     */
    boolean batchInsertMesOperationLog(List<MesOperationLog> mesOperationLogs);

    /**
     * 导出系统运维日志
     *
     * @param mesOperationLog 系统运维日志
     * @param response
     */

    void exportMesOperationLog(MesOperationLog mesOperationLog, HttpServletResponse response);

    /**
     * 根据分组名查询数量
     * @param mesOperationLog
     * @return
     */
    List<MesOperationLog> selectCountByDimension(MesOperationLog mesOperationLog);
}
