package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.flowable.domain.dto.MesReleaseLog;
import com.ruoyi.flowable.mapper.MesReleaseLogMapper;
import com.ruoyi.flowable.service.IMesReleaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-04-18
 */
@Service
public class MesReleaseLogServiceImpl extends ServiceImpl<MesReleaseLogMapper, MesReleaseLog> implements IMesReleaseLogService {
    @Autowired
    private MesReleaseLogMapper mesReleaseLogMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param releaseVersion 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesReleaseLog selectMesReleaseLogByReleaseVersion(String releaseVersion)
    {
        return mesReleaseLogMapper.selectById(releaseVersion);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesReleaseLog> selectMesReleaseLogList(MesReleaseLog mesReleaseLog)
    {
        return mesReleaseLogMapper.selectMesReleaseLogList(mesReleaseLog);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMesReleaseLog(MesReleaseLog mesReleaseLog)
    {
        return mesReleaseLogMapper.insert(mesReleaseLog);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMesReleaseLog(MesReleaseLog mesReleaseLog)
    {
        return mesReleaseLogMapper.updateById(mesReleaseLog);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param releaseVersions 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMesReleaseLogByReleaseVersions(List<String> releaseVersions)
    {
        return mesReleaseLogMapper.deleteBatchIds(releaseVersions);
    }

    @Override
    public int getCount(Long year, Long month) {
        return mesReleaseLogMapper.getCount(year,month);
    }

    @Override
    public String getReleaseVersion() {
        Date date = new Date();
        long year = date.getYear() + 1900;
        long month = date.getMonth() + 1;
        int count = getCount(year, month);
        return "v" + (year - 2023) + "." + month + "." + (count + 1) + "_" + DateUtils.parseDateToStr("yyyyMMddHHmmss", date);
    }
}