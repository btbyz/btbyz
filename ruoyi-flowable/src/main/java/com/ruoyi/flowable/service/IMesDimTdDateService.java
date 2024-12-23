package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesDimTdDate;

import java.util.List;

/**
 * 日历配置Service接口
 * 
 * @author ruoyi
 * @date 2024-09-07
 */
public interface IMesDimTdDateService extends IService<MesDimTdDate>
{
    List<MesDimTdDate> generateCalendar();

    boolean batchInsertMesDimTdDate(List<MesDimTdDate> mesDimTdDateList);

    /**
     * 查询日历配置
     * 
     * @param fullDate 日历配置主键
     * @return 日历配置
     */
    public MesDimTdDate selectMesDimTdDateByFullDate(String fullDate);

    /**
     * 查询日历配置列表
     * 
     * @param mesDimTdDate 日历配置
     * @return 日历配置集合
     */
    public List<MesDimTdDate> selectMesDimTdDateList(MesDimTdDate mesDimTdDate);

    /**
     * 新增日历配置
     * 
     * @param mesDimTdDate 日历配置
     * @return 结果
     */
    public int insertMesDimTdDate(MesDimTdDate mesDimTdDate);

    /**
     * 修改日历配置
     * 
     * @param mesDimTdDate 日历配置
     * @return 结果
     */
    public int updateMesDimTdDate(MesDimTdDate mesDimTdDate);

    /**
     * 批量删除日历配置
     * 
     * @param fullDates 需要删除的日历配置主键集合
     * @return 结果
     */
    public int deleteMesDimTdDateByFullDates(String[] fullDates);

    /**
     * 删除日历配置信息
     * 
     * @param fullDate 日历配置主键
     * @return 结果
     */
    public int deleteMesDimTdDateByFullDate(String fullDate);
}
