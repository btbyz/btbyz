package com.ruoyi.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flowable.domain.dto.MesWorkTime;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2024-04-10
 */
public interface MesWorkTimeMapper extends BaseMapper<MesWorkTime> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesWorkTime> selectMesWorkTimeList(MesWorkTime mesWorkTime);


    public List<MesWorkTime> selectDailyCompleteTimeCardList(MesWorkTime mesWorkTime);

    public List<MesWorkTime> selectWeeklyCompleteTimeCardList(MesWorkTime mesWorkTime);

    public List<MesWorkTime> selectTimeCardListForOwn(MesWorkTime mesWorkTime);

    public List<MesWorkTime> selectTotalTimeCardListForOwn(MesWorkTime mesWorkTime);

    public List<MesWorkTime> selectWorkTypeDuration(MesWorkTime mesWorkTime);

    public MesWorkTime selectWeeklyTotalTime(MesWorkTime mesWorkTime);

    public MesWorkTime selectMaxWorkTime(MesWorkTime mesWorkTime);
}
