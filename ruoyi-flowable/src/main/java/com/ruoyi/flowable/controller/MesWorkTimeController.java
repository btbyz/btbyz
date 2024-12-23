package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesWorkTime;
import com.ruoyi.flowable.service.IMesWorkTimeService;
import com.ruoyi.flowable.util.CurrentWeekUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 日报Controller
 *
 * @author ruoyi
 * @date 2024-04-10
 */
@RestController
@RequestMapping("/mes/time")
public class MesWorkTimeController extends BaseController {
    @Autowired
    private IMesWorkTimeService mesWorkTimeService;

    /**
     * 查询日报列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesWorkTime mesWorkTime) {
        startPage();
        List<MesWorkTime> list = mesWorkTimeService.selectMesWorkTimeList(mesWorkTime);
        return getDataTable(list);
    }

    /**
     * 导出日报列表
     */
    @Log(title = "导出日报", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesWorkTime mesWorkTime) {
        List<MesWorkTime> list = mesWorkTimeService.selectMesWorkTimeList(mesWorkTime);
        ExcelUtil<MesWorkTime> util = new ExcelUtil<MesWorkTime>(MesWorkTime.class);
        util.exportExcel(response, list, "导出日报数据");
    }

    /**
     * 获取日报详细信息
     */
    @GetMapping(value = "/get/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(mesWorkTimeService.selectMesWorkTimeById(id));
    }

    /**
     * 新增日报
     */
    @Log(title = "新增日报", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult add(@RequestBody MesWorkTime mesWorkTime) {
        return toAjax(mesWorkTimeService.insertMesWorkTime(mesWorkTime));
    }

    /**
     * 修改日报
     */
    @Log(title = "修改日报", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody MesWorkTime mesWorkTime) {
        return toAjax(mesWorkTimeService.updateMesWorkTime(mesWorkTime));
    }

    /**
     * 删除日报
     */
    @Log(title = "删除日报", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable List<String> ids) {
        return toAjax(mesWorkTimeService.deleteMesWorkTimeByIds(ids));
    }

    /**
     * 获取时间范围
     * */
    @Log(title = "获取时间范围", businessType = BusinessType.QUERY)
    @GetMapping("/get/date-range/{year}")
    public AjaxResult getDateRange(@PathVariable Integer year) {
        MesWorkTime mesWorkTime = new MesWorkTime();
        Map<String,String> map = CurrentWeekUtils.getDateRange(year);
        mesWorkTime.setMap(map);
        return AjaxResult.success(mesWorkTime);
    }

    /**
     * 获取当前周
     * */
    @Log(title = "获取当前周", businessType = BusinessType.QUERY)
    @PostMapping("/get/curr-week")
    public AjaxResult getCurrWeek(@RequestBody MesWorkTime mesWorkTime) {
        MesWorkTime workTime = new MesWorkTime();
        String currWeek = CurrentWeekUtils.getCurrentWeek(mesWorkTime.getDateNow());
        workTime.setWeekOfYear(currWeek);
        return AjaxResult.success(workTime);
    }

    /**
     * 获取填报人
     * */
    @Log(title = "获取填报人", businessType = BusinessType.QUERY)
    @GetMapping("/get/work_user")
    public AjaxResult getWorkUser() {
        return AjaxResult.success(mesWorkTimeService.getWorkUser());
    }

    /**
     * 获取登录用户一周timecard填写记录
     * */
    @Log(title = "获取登录用户一周timecard填写记录", businessType = BusinessType.QUERY)
    @PostMapping("/selectTimeCardList")
    public AjaxResult selectTimeCardList(@RequestBody MesWorkTime mesWorkTime) {
        return AjaxResult.success(mesWorkTimeService.selectTimeCardList(mesWorkTime));
    }

    /**
     * 获取用户每周的几种工作类型占比
     * @param mesWorkTime
     * @return
     */
    @PostMapping("/getProportionOfWorkTypes")
    public AjaxResult getProportionOfWorkTypes(@RequestBody MesWorkTime mesWorkTime) {
        return AjaxResult.success(mesWorkTimeService.getProportionOfWorkTypes(mesWorkTime));
    }
    /**
     * 查询当前任务的上一条工时管理填写记录
     */
    @PostMapping("/getLastTimeCardInfo")
    @Log(title = "查询当前任务的上一条工时管理填写记录", businessType = BusinessType.QUERY)
    public AjaxResult getLastTimeCardInfo(@RequestBody MesWorkTime mesWorkTime) {
        return AjaxResult.success(mesWorkTimeService.getLastTimeCardInfo(mesWorkTime));
    }

    /**
     * 查询当前填写日期的前一个工作日是否满足8小时
     */
    @PostMapping("/getLastWorkDayMsg")
    @Log(title = "查询当前填写日期的前一个工作日的填写时长", businessType = BusinessType.QUERY)
    public AjaxResult getLastWorkDayMsg(@RequestBody MesWorkTime mesWorkTime) {
        return AjaxResult.success(mesWorkTimeService.getLastWorkDayMsg(mesWorkTime));
    }

}
