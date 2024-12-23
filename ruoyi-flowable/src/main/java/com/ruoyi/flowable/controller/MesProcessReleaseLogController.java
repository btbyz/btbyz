package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.service.IMesProcessReleaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程发布日志 mes_process_release_log
 *
 * @author zhangtao
 * @date 2024-03-25
 */


@RestController
@RequestMapping("/mes/process-release-log")
public class MesProcessReleaseLogController extends BaseController {

    @Autowired
    private IMesProcessReleaseLogService mesProcessReleaseLogService;

    /**
     * 查询流程发布日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesProcessReleaseLog mesProcessReleaseLog) {
        startPage();
        List<MesProcessReleaseLog> list = mesProcessReleaseLogService.selectMesProcessReleaseLogList(mesProcessReleaseLog);
        return getDataTable(list);
    }

    /**
     * 导出流程发布日志列表
     *
     */
    @Log(title = "需求项目记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(@RequestBody MesProcessReleaseLog mesProcessReleaseLog) {
        List<MesProcessReleaseLog> list = mesProcessReleaseLogService.selectMesProcessReleaseLogList(mesProcessReleaseLog);
        return AjaxResult.success(list);
    }

    /**
     * 获取流程发布日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(mesProcessReleaseLogService.selectMesProcessReleaseLogById(id));
    }

    /**
     * 新增流程发布日志
     */
    @Log(title = "需求项目记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MesProcessReleaseLog mesProcessReleaseLog) {
        return toAjax(mesProcessReleaseLogService.insertMesProcessReleaseLog(mesProcessReleaseLog));
    }

    /**
     * 修改流程发布日志
     */
    @Log(title = "需求项目记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody MesProcessReleaseLog mesProcessReleaseLog) {
        return toAjax(mesProcessReleaseLogService.updateMesProcessReleaseLog(mesProcessReleaseLog));
    }

    /**
     * 删除流程发布日志
     */
    @Log(title = "需求项目记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long[] id) {
        return toAjax(mesProcessReleaseLogService.deleteMesProcessReleaseLogByIds(id));
    }

    /**
     * 获取流程发布日志版本信息
     */
    @Log(title = "需求项目记录", businessType = BusinessType.QUERY)
    @GetMapping("/version")
    public AjaxResult getVersion() {
        return AjaxResult.success(mesProcessReleaseLogService.getVersion());
    }
    
}
