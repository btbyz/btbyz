package com.ruoyi.flowable.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.flowable.config.CalendarProperties;
import com.ruoyi.flowable.domain.dto.MesDimTdDate;
import com.ruoyi.flowable.domain.resp.CalendarificResponse;
import com.ruoyi.flowable.mapper.MesDimTdDateMapper;
import com.ruoyi.flowable.service.IMesDimTdDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;



/**
 * 日历配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-07
 */
@Service
public class MesDimTdDateServiceImpl extends ServiceImpl<MesDimTdDateMapper,MesDimTdDate> implements IMesDimTdDateService
{
    @Autowired
    private MesDimTdDateMapper mesDimTdDateMapper;

    @Autowired
    private IMesDimTdDateService mesDimTdDateService;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private CalendarProperties calendarProperties;

    /**
     * 调用calendarific.com 生成日历配置
     * @param
     */
    @Override
    public List<MesDimTdDate> generateCalendar() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.valueOf(now.getMonthValue() + 1);
        JSONObject response =  restTemplate.getForObject(calendarProperties.getAddress(),JSONObject.class,
                calendarProperties.getCountry(),year,calendarProperties.getLanguage(),calendarProperties.getApiKey(),month);
        FabAssertUtils.isNotNull(response, BaseErrorCodeEnum.PARAM_ERROR,"获取节假日数据失败！");
        List<CalendarificResponse> holidays = response.getJSONObject("response").getJSONArray("holidays").toJavaList(CalendarificResponse.class);
        Map<String, String> holidayMap = holidays.stream()
                .collect(Collectors.toMap(
                        holiday -> holiday.getDate().getIso(),
                        holiday -> CollectionUtils.isNotEmpty(holiday.getType()) ? holiday.getType().get(0) : ""
                ));
        // 根据给定的年份和月份，循环生成当月的数据
        // 起始日期 和 结束日期
        LocalDate startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<MesDimTdDate> mesDimTdDateList = new ArrayList<>();
        for (LocalDate date = startDate;!date.isAfter(endDate); date = date.plusDays(1)) {
            MesDimTdDate mesDimTdDate = new MesDimTdDate();
            String dateStr = date.format(DateTimeFormatter.ISO_DATE);
            // 日期
            mesDimTdDate.setFullDate(dateStr);
            // 一周中的第几天
            mesDimTdDate.setDayOfWeek(date.getDayOfWeek().getValue());
            // 一个月中的第几天
            mesDimTdDate.setDayNumInMonth(date.getDayOfMonth());
            // 是否为工作日 周一到周五 + 周末调休
            mesDimTdDate.setWeekdayFlag((date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY) && !Objects.equals(holidayMap.get(dateStr) ,"Weekend")
                    ? "N" : "Y");
            // 第几周
            mesDimTdDate.setWeekNumInYear(date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            // 当前周从哪天开始
            mesDimTdDate.setWeekBeginDate(String.valueOf(date.with(DayOfWeek.MONDAY)));
            // 月份
            mesDimTdDate.setMonth(date.getMonthValue());
            // 年份
            mesDimTdDate.setYear(date.getYear());
            // 是否为节假日
            mesDimTdDate.setVacationDayFlag(holidayMap.containsKey(dateStr) && Objects.equals(holidayMap.get(dateStr), "National holiday") ? "Y" : "N");
            // 年W周
            mesDimTdDate.setWeekStrInYear(mesDimTdDate.getYear() + "W" + mesDimTdDate.getWeekNumInYear());
            mesDimTdDateList.add(mesDimTdDate);
        }
        mesDimTdDateService.saveOrUpdateBatch(mesDimTdDateList);
        return mesDimTdDateList;
    }

    /**
     * 批量新增日历配置
     */
    @Override
    public boolean batchInsertMesDimTdDate(List<MesDimTdDate> mesDimTdDateList){
        return this.saveBatch(mesDimTdDateList);
    }

    /**
     * 查询日历配置
     * 
     * @param fullDate 日历配置主键
     * @return 日历配置
     */
    @Override
    public MesDimTdDate selectMesDimTdDateByFullDate(String fullDate)
    {
        return mesDimTdDateMapper.selectMesDimTdDateByFullDate(fullDate);
    }

    /**
     * 查询日历配置列表
     * 
     * @param mesDimTdDate 日历配置
     * @return 日历配置
     */
    @Override
    public List<MesDimTdDate> selectMesDimTdDateList(MesDimTdDate mesDimTdDate)
    {
        return mesDimTdDateMapper.selectMesDimTdDateList(mesDimTdDate);
    }

    /**
     * 新增日历配置
     * 
     * @param mesDimTdDate 日历配置
     * @return 结果
     */
    @Override
    public int insertMesDimTdDate(MesDimTdDate mesDimTdDate)
    {
        return mesDimTdDateMapper.insertMesDimTdDate(mesDimTdDate);
    }


    /**
     * 修改日历配置
     * 
     * @param mesDimTdDate 日历配置
     * @return 结果
     */
    @Override
    public int updateMesDimTdDate(MesDimTdDate mesDimTdDate)
    {
        return mesDimTdDateMapper.updateMesDimTdDate(mesDimTdDate);
    }

    /**
     * 批量删除日历配置
     * 
     * @param fullDates 需要删除的日历配置主键
     * @return 结果
     */
    @Override
    public int deleteMesDimTdDateByFullDates(String[] fullDates)
    {
        return mesDimTdDateMapper.deleteMesDimTdDateByFullDates(fullDates);
    }

    /**
     * 删除日历配置信息
     * 
     * @param fullDate 日历配置主键
     * @return 结果
     */
    @Override
    public int deleteMesDimTdDateByFullDate(String fullDate)
    {
        return mesDimTdDateMapper.deleteMesDimTdDateByFullDate(fullDate);
    }
}
