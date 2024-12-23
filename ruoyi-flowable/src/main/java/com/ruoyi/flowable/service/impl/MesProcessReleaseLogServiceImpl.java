package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.mapper.MesProcessReleaseLogMapper;
import com.ruoyi.flowable.service.IMesProcessReleaseLogService;
import com.ruoyi.flowable.service.IMesProcessReleaseLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 需求项目记录Service业务层处理
 *
 * @author wangxin
 * @date 2024-03-11
 */
@Service
public class MesProcessReleaseLogServiceImpl implements IMesProcessReleaseLogService {
    @Resource
    private  MesProcessReleaseLogMapper MesProcessReleaseLogMapper;
    /**
     * 查询需求项目记录
     *
     * @param id 需求项目记录ID
     * @return 需求项目记录
     */
    @Override
    public MesProcessReleaseLog selectMesProcessReleaseLogById(Long id) {
        return MesProcessReleaseLogMapper.selectMesProcessReleaseLogById(id);
    }

    /**
     * 查询需求项目记录列表
     *
     * @param mesProcessReleaseLog 需求项目记录
     * @return 需求项目记录
     */
    @Override
    public List<MesProcessReleaseLog> selectMesProcessReleaseLogList(MesProcessReleaseLog mesProcessReleaseLog) {
        return MesProcessReleaseLogMapper.selectMesProcessReleaseLogList(mesProcessReleaseLog);
    }

    /**
     * 新增需求项目记录
     *
     * @param mesProcessReleaseLog 需求项目记录
     * @return 结果
     */
    @Override
    public boolean insertMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog) {
        return MesProcessReleaseLogMapper.insertMesProcessReleaseLog(mesProcessReleaseLog);
    }

    /**
     * 修改需求项目记录
     *
     * @param mesProcessReleaseLog 需求项目记录
     * @return 结果
     */
    @Override
    public boolean updateMesProcessReleaseLog(MesProcessReleaseLog mesProcessReleaseLog) {
        return MesProcessReleaseLogMapper.updateMesProcessReleaseLog(mesProcessReleaseLog);
    }

    /**
     * 批量删除需求项目记录
     *
     * @param id 需要删除的需求项目记录ID
     * @return 结果
     */
    @Override
    public boolean deleteMesProcessReleaseLogByIds(Long[] id) {
        if ( id == null || id.length == 0) {
            return false;
        }
        return MesProcessReleaseLogMapper.deleteMesProcessReleaseLog(id);
    }

    @Override
    public List<String> getVersion() {
        List<String> list =  MesProcessReleaseLogMapper.getVersion();
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().distinct().collect(Collectors.toList());
        } else {
          return new ArrayList<>();
        }
    }

}
