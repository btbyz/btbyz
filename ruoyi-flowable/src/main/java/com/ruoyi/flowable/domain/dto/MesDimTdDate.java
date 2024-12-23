package com.ruoyi.flowable.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntityDto;
import lombok.*;

/**
 * 日历配置对象 mes_dim_td_date
 * 
 * @author ruoyi
 * @date 2024-09-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class MesDimTdDate extends BaseEntityDto
{
    private static final long serialVersionUID = 1L;

    /** 日期 */
    @TableId
    private String fullDate;

    /** 一周的第几天 */
    @Excel(name = "一周的第几天")
    private Integer dayOfWeek;

    /** 一个月的第几天 */
    @Excel(name = "一个月的第几天")
    private Integer dayNumInMonth;


    /** 是否是工作日（N：不是，Y：是） */
    @Excel(name = "是否是工作日", readConverterExp = "N=：不是，Y：是")
    private String weekdayFlag;

    /** 一年的第几周 */
    @Excel(name = "一年的第几周")
    private Integer weekNumInYear;

    /** 当前周从哪天开始 */
    @Excel(name = "当前周从哪天开始")
    private String weekBeginDate;

    /** 一年中第几个月 */
    @Excel(name = "一年中第几个月")
    private Integer month;

    /** 年份 */
    @Excel(name = "年份")
    private Integer year;

    /** 是否是节假日（N：不是，Y：是） */
    @Excel(name = "是否是节假日", readConverterExp = "N=：不是，Y：是")
    private String vacationDayFlag;

    /** 年W周 */
    @Excel(name = "年W周")
    private String weekStrInYear;


}
