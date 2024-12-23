package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesDimTdDate;
import com.ruoyi.flowable.service.IMesDimTdDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日历配置Controller
 * 
 * @author ruoyi
 * @date 2024-09-07
 */
@RestController
@RequestMapping("/flowable/date")
public class MesDimTdDateController extends BaseController
{
    @Autowired
    private IMesDimTdDateService mesDimTdDateService;

    /**
     * 查询日历配置列表
     */
    @Log(title = "查询日历配置列表", businessType = BusinessType.QUERY)
    @GetMapping("/list")
    public TableDataInfo list(MesDimTdDate mesDimTdDate)
    {
        startPage();
        List<MesDimTdDate> list = mesDimTdDateService.selectMesDimTdDateList(mesDimTdDate);
        return getDataTable(list);
    }

    /**
     * 导出日历配置列表
     */
    @Log(title = "日历配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesDimTdDate mesDimTdDate)
    {
        List<MesDimTdDate> list = mesDimTdDateService.selectMesDimTdDateList(mesDimTdDate);
        ExcelUtil<MesDimTdDate> util = new ExcelUtil<MesDimTdDate>(MesDimTdDate.class);
        util.exportExcel(response, list, "日历配置数据");
    }

    /**
     * 获取日历配置详细信息
     */
    @Log(title = "获取日历配置详细信息", businessType = BusinessType.QUERY)
    @GetMapping(value = "/{fullDate}")
    public AjaxResult getInfo(@PathVariable("fullDate") String fullDate)
    {
        return success(mesDimTdDateService.selectMesDimTdDateByFullDate(fullDate));
    }

    /**
     * 新增日历配置
     */
    @Log(title = "日历配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MesDimTdDate mesDimTdDate)
    {
        return toAjax(mesDimTdDateService.insertMesDimTdDate(mesDimTdDate));
    }

    /**
     * 修改日历配置
     */
    @Log(title = "日历配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MesDimTdDate mesDimTdDate)
    {
        return toAjax(mesDimTdDateService.updateMesDimTdDate(mesDimTdDate));
    }

    /**
     * 删除日历配置
     */
    @Log(title = "日历配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{fullDates}")
    public AjaxResult remove(@PathVariable String[] fullDates)
    {
        return toAjax(mesDimTdDateService.deleteMesDimTdDateByFullDates(fullDates));
    }

    /**
     * 测试定时任务
     */
    @Log(title = "测试生成日历定时任务", businessType = BusinessType.OTHER)
    @GetMapping("/testGenerate")
    public void testGenerateCalendar()
    {
         mesDimTdDateService.generateCalendar();
    }
}
