package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesOperationLog;

import java.util.List;

/**
 * 系统运维日志Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
public interface MesOperationLogMapper extends BaseMapper<MesOperationLog>
{
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
     * 批量删除系统运维日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMesOperationLogByIds(Long[] ids);

    public List<MesOperationLog> selectCountByDimension(MesOperationLog mesOperationLog);
}
