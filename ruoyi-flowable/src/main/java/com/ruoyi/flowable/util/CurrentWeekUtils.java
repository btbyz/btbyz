package com.ruoyi.flowable.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrentWeekUtils {

    /**
     * 生成当前周
     * 例：27
     *
     * @param date 日期
     * @return 当前周
     * */
    public static String getCurrentWeek(Date date) {
        // 将java.util.Date转换为java.time.LocalDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate localDate = LocalDate.of(year, month, day);
        int weekOfYear = localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR); // 获取当前年的周数
        return year + "W" + weekOfYear;
    }

    /**
     * 获取当前年份的总周数一以及
     * */
    public static Map<String, String> getDateRange(Integer year) {
        DateFormat dateFormatY = new SimpleDateFormat("Y");
        // 采用ISO标准计算全年总周数，第一周至少需要有四天
        WeekFields weekFields = WeekFields.ISO;
        int num = LocalDate.of(year, 12, 31).get(weekFields.weekOfWeekBasedYear());
        num = num == 1 ? 52 : num;
        Map<String, String> map = new HashMap<>(54);
        for (int i = 1; i <= num; i++) {
            // 获取每周的起始日期
            LocalDate weekStart =  getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.MONDAY);
            // 获取每周的结束日期
            LocalDate weekEnd =  getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.SUNDAY);
            String weekKey = year + "W" + i;
            String weekValue = weekStart + "至" + weekEnd;
            map.put(weekKey, weekValue);
        }
        System.out.println(map);
        return map;
    }

    private static LocalDate getDateByYearAndWeekNumAndDayOfWeek(Integer year, Integer num, DayOfWeek dayOfWeek) {
        //周数小于10在前面补个0
        String numStr = num < 10 ? "0" + String.valueOf(num) : String.valueOf(num);
        //2019-W01-01获取第一周的周一日期，2019-W02-07获取第二周的周日日期
        String weekDate = String.format("%s-W%s-%s", year, numStr, dayOfWeek.getValue());
        return LocalDate.parse(weekDate, DateTimeFormatter.ISO_WEEK_DATE);
    }

//    public static void main(String[] args) {
//        DateFormat dateFormatY = new SimpleDateFormat("Y");
//        int year = LocalDate.now().getYear();
//        // 采用ISO标准计算全年总周数，第一周至少需要有四天
//        WeekFields weekFields = WeekFields.ISO;
//        int num = LocalDate.of(year, 12, 31).get(weekFields.weekOfWeekBasedYear());
//        num = num == 1 ? 52 : num;
//        Map<String, String> map = new HashMap<>(54);
//        for (int i = 1; i <= num; i++) {
//            // 获取每周的起始日期
//            LocalDate weekStart =  getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.MONDAY);
//            // 获取每周的结束日期
//            LocalDate weekEnd =  getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.SUNDAY);
//            String weekKey = year + "-" + i;
//            String weekValue = weekStart + "至" + weekEnd;
//            map.put(weekKey, weekValue);
//            System.out.println(weekKey+ ":" + weekValue);
//            System.out.println("第" + i + "周，周一日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.MONDAY));
//            System.out.println("第" + i + "周，周二日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.TUESDAY));
//            System.out.println("第" + i + "周，周三日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.WEDNESDAY));
//            System.out.println("第" + i + "周，周四日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.THURSDAY));
//            System.out.println("第" + i + "周，周五日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.FRIDAY));
//            System.out.println("第" + i + "周，周六日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.SATURDAY));
//            System.out.println("第" + i + "周，周日日期:" + getDateByYearAndWeekNumAndDayOfWeek(year, i, DayOfWeek.SUNDAY));
//            System.out.println("----------------------------------");
//
//        }
//    }

}
