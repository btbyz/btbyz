package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesReleaseLog;
import com.ruoyi.flowable.service.IMesReleaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-04-18
 */
@RestController
@RequestMapping("/mes/release/log")
public class MesReleaseLogController extends BaseController
{
    @Autowired
    private IMesReleaseLogService mesReleaseLogService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesReleaseLog mesReleaseLog)
    {
        startPage();
        List<MesReleaseLog> list = mesReleaseLogService.selectMesReleaseLogList(mesReleaseLog);
        return getDataTable(list);
    }

    /**
     * 导出发布版本信息
     */
    @Log(title = "导出发布版本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesReleaseLog mesReleaseLog)
    {
        List<MesReleaseLog> list = mesReleaseLogService.selectMesReleaseLogList(mesReleaseLog);
        ExcelUtil<MesReleaseLog> util = new ExcelUtil<MesReleaseLog>(MesReleaseLog.class);
        util.exportExcel(response, list, "布版本信息数据");
    }

    /**
     * 获取发布版本详细信息
     */
    @GetMapping(value = "/get/{releaseVersion}")
    public AjaxResult getInfo(@PathVariable("releaseVersion") String releaseVersion)
    {
        return success(mesReleaseLogService.selectMesReleaseLogByReleaseVersion(releaseVersion));
    }

    /**
     * 新增发布版本信息
     */
    @Log(title = "新增发布版本信息", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult add(@RequestBody MesReleaseLog mesReleaseLog)
    {
        return toAjax(mesReleaseLogService.insertMesReleaseLog(mesReleaseLog));
    }

    /**
     * 修改发布版本信息
     */
    @Log(title = "修改发布版本信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody MesReleaseLog mesReleaseLog)
    {
        return toAjax(mesReleaseLogService.updateMesReleaseLog(mesReleaseLog));
    }

    /**
     * 删除发布版本信息
     */
    @Log(title = "删除发布版本信息", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{releaseVersions}")
    public AjaxResult remove(@PathVariable List<String> releaseVersions)
    {
        return toAjax(mesReleaseLogService.deleteMesReleaseLogByReleaseVersions(releaseVersions));
    }

    /**
     * 获取发布版本号
     * */
    @Log(title = "获取发布版本号")
    @GetMapping("/get/release/version")
    public AjaxResult getReleaseVersion() {
        return success(mesReleaseLogService.getReleaseVersion());
    }
}