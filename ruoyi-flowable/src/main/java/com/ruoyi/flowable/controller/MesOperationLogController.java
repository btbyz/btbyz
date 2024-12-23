package com.ruoyi.flowable.controller;

import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.flowable.domain.dto.MesOperationLog;
import com.ruoyi.flowable.service.IMesOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统运维日志Controller
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
@RestController
@RequestMapping("/system/log")
public class MesOperationLogController extends BaseController
{
    @Autowired
    private IMesOperationLogService mesOperationLogService;

    /**
     * 查询系统运维日志列表
     */
    @GetMapping("/list")
    public AjaxResult list(MesOperationLog mesOperationLog)
    {
        startPage();
        List<MesOperationLog> list = mesOperationLogService.selectMesOperationLogList(mesOperationLog);
        List<MesOperationLog> countList = mesOperationLogService.selectCountByDimension(mesOperationLog);
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("data",list);
        returnMap.put("count",countList);
        returnMap.put("total",new PageInfo(list).getTotal());
        return success(returnMap);
    }

    /**
     * 导出系统运维日志列表
     */
    @Log(title = "系统运维日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody MesOperationLog mesOperationLog, HttpServletResponse response)
    {
        mesOperationLogService.exportMesOperationLog(mesOperationLog, response);
    }

    /**
     * 获取系统运维日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mesOperationLogService.selectMesOperationLogById(id));
    }

    /**
     * 新增系统运维日志
     */
    @Log(title = "系统运维日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MesOperationLog mesOperationLog)
    {
        return toAjax(mesOperationLogService.insertMesOperationLog(mesOperationLog));
    }

    /**
     * 批量新增系统运维日志
     */
    @Log(title = "系统运维日志", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody List<MesOperationLog> mesOperationLogs){
         return toAjax(mesOperationLogService.batchInsertMesOperationLog(mesOperationLogs));
    }


    /**
     * 修改系统运维日志
     */
    @Log(title = "系统运维日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MesOperationLog mesOperationLog)
    {
        return toAjax(mesOperationLogService.updateMesOperationLog(mesOperationLog));
    }

    /**
     * 删除系统运维日志
     */
    @Log(title = "系统运维日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mesOperationLogService.deleteMesOperationLogByIds(ids));
    }

    /**
     * 获取系统日志获取 系统各分类下的运维数量
     */
//    @Log(title = "系统运维日志数量", businessType = BusinessType.QUERY)
//    @PostMapping("/getLogCount")
//    public AjaxResult getLogCount(@RequestBody MesOperationLog mesOperationLog){
//        return success(mesOperationLogService.getLogCount(mesOperationLog));
//    }

}
