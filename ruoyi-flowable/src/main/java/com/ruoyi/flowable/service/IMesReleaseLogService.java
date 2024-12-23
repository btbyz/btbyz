package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesReleaseLog;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-04-15
 */
public interface IMesReleaseLogService extends IService<MesReleaseLog> {
    /**
     * 查询【请填写功能名称】
     *
     * @param releaseVersion 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesReleaseLog selectMesReleaseLogByReleaseVersion(String releaseVersion);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesReleaseLog> selectMesReleaseLogList(MesReleaseLog mesReleaseLog);

    /**
     * 新增【请填写功能名称】
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 结果
     */
    public int insertMesReleaseLog(MesReleaseLog mesReleaseLog);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesReleaseLog 【请填写功能名称】
     * @return 结果
     */
    public int updateMesReleaseLog(MesReleaseLog mesReleaseLog);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param releaseVersions 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteMesReleaseLogByReleaseVersions(List<String> releaseVersions);

    int getCount(Long year, Long month);

    String getReleaseVersion();


}