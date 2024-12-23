package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesRequirementTask;
import com.ruoyi.flowable.service.IMesRequirementTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-04-10
 */
@RestController
@RequestMapping("/mes/task")
public class MesRequirementTaskController extends BaseController
{
    @Autowired
    private IMesRequirementTaskService mesRequirementTaskService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesRequirementTask mesRequirementTask)
    {
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        // 110表示该账号可以看到制造信息部下的所有需求数据，包含电池MES部、组件WMS部...的数据
        // 非110的账号只能看到对应部门下的数据
        if (sysDept.getDeptId() != 110) {
            mesRequirementTask.setDeptId(sysDept.getDeptId());
        }
        startPage();
        List<MesRequirementTask> list = mesRequirementTaskService.selectMesRequirementTaskList(mesRequirementTask);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesRequirementTask mesRequirementTask)
    {
        List<MesRequirementTask> list = mesRequirementTaskService.selectMesRequirementTaskList(mesRequirementTask);
        ExcelUtil<MesRequirementTask> util = new ExcelUtil<MesRequirementTask>(MesRequirementTask.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取详细信息
     */
    @GetMapping(value = "/get/{taskCode}")
    public AjaxResult getInfo(@PathVariable("taskCode") String taskCode)
    {
        return success(mesRequirementTaskService.selectMesRequirementTaskByTaskCode(taskCode));
    }

    /**
     * 新增任务
     */
    @Log(title = "新增任务", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult add(@RequestBody MesRequirementTask mesRequirementTask)
    {
        return toAjax(mesRequirementTaskService.insertMesRequirementTask(mesRequirementTask));
    }

    /**
     * 指派任务
     */
    @Log(title = "指派任务", businessType = BusinessType.INSERT)
    @PostMapping("/assign/task")
    public AjaxResult assignTask(@RequestBody MesRequirementTask mesRequirementTask)
    {
        return toAjax(mesRequirementTaskService.assignTask(mesRequirementTask));
    }

    /**
     * 修改任务
     */
    @Log(title = "修改任务", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody MesRequirementTask mesRequirementTask)
    {
        return toAjax(mesRequirementTaskService.updateMesRequirementTask(mesRequirementTask));
    }

    /**
     * 删除任务
     */
    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{taskCodes}")
    public AjaxResult remove(@PathVariable List<String> taskCodes)
    {
        return toAjax(mesRequirementTaskService.deleteMesRequirementTaskByTaskCodes(taskCodes));
    }

    /**
     * 获取任务编号
     * */
    @Log(title = "获取任务编号", businessType = BusinessType.QUERY)
    @GetMapping("/get/task-code")
    public AjaxResult getTaskCodes()
    {
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        // 110表示该账号可以看到制造信息部下的所有需求数据，包含电池MES部、组件WMS部...的数据
        // 非110的账号只能看到对应部门下的数据
        Long systemModule = null;
        if (sysDept.getDeptId() != 110) {
            systemModule = sysDept.getDeptId();
        }
        return AjaxResult.success(mesRequirementTaskService.getTaskCodes(systemModule));
    }

    /**
     * 获取任务
     * */
    @Log(title = "获取任务", businessType = BusinessType.QUERY)
    @PostMapping("/get/task/name")
    public AjaxResult getTasksByUserName(@RequestBody MesRequirementTask mesRequirementTask)
    {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        mesRequirementTask.setDevelopers(sysUser.getUserName());
        return AjaxResult.success(mesRequirementTaskService.selectMesRequirementTaskListForTimeCard(mesRequirementTask));
    }


//    /**
//     * 申请发布流程
//     * */
//    @Log(title = "申请发布流程", businessType = BusinessType.UPDATE)
//    @PostMapping("/generate/{id}")
//    public AjaxResult generateReleaseDocument(@PathVariable Long[] id) {
//        return toAjax(mesProjectRecordsService.generateReleaseDocument(id));
//    }

    /**
     * 查找需求下任务的最小计划开始时间和最大计划结束时间
     */
    @Log(title = "查找计划时间范围", businessType = BusinessType.QUERY)
    @PostMapping("/selectTimeEndpoint")
    public AjaxResult selectTimeEndpoint(@RequestBody MesRequirementTask mesRequirementTask) {
        return AjaxResult.success(mesRequirementTaskService.selectTimeEndpoint(mesRequirementTask));
    }
}
