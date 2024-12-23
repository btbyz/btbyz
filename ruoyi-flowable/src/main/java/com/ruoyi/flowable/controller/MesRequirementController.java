package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.flowable.domain.dto.MesRequirement;
import com.ruoyi.flowable.service.IMesRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-04-08
 */
@RestController
@RequestMapping("/mes/requirement")
public class MesRequirementController extends BaseController {
    @Autowired
    private IMesRequirementService mesRequirementService;

    /**
     * 查询需求列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesRequirement mesRequirement) {
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        // 110表示该账号可以看到制造信息部下的所有需求数据，包含电池MES部、组件WMS部...的数据
        // 非110的账号只能看到对应部门下的数据
//        if (sysDept.getDeptId() != 110L) {
            Long deptId = sysDept.getDeptId();
            mesRequirement.setDeptId(deptId);
//        }
        startPage();
        List<MesRequirement> list = mesRequirementService.selectMesRequirementList(mesRequirement);
        return getDataTable(list);
    }

    /**
     * 导出需求
     */
    @Log(title = "导出需求清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@RequestBody MesRequirement mesRequirement, HttpServletResponse response) {
        mesRequirementService.export(mesRequirement, response);
    }

    /**
     * 获取需求
     */
    @GetMapping(value = "/get/{requirementCode}")
    public AjaxResult getInfo(@PathVariable("requirementCode") String requirementCode) {
        return success(mesRequirementService.selectMesRequirementByRequirementCode(requirementCode));
    }

    /**
     * 获取需求明细数据 包括需求明细+任务明细数据
     */
    @Log(title = "获取需求明细数据", businessType = BusinessType.QUERY)
    @PostMapping("/get/requirementDetails")
    public AjaxResult getRequirementsDetails(@RequestBody MesRequirement mesRequirement) {
        return AjaxResult.success(mesRequirementService.getRequirementsDetails(mesRequirement));
    }

    /**
     * 新增需求
     */
    @Log(title = "新增需求", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult add(@RequestBody MesRequirement mesRequirement) {
        return toAjax(mesRequirementService.insertMesRequirement(mesRequirement));
    }

    /**
     * 修改需求
     */
    @Log(title = "修改需求", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody MesRequirement mesRequirement) {
        boolean result = mesRequirementService.updateRequirementAndTask(mesRequirement);
        if (!result) {
            return AjaxResult.error("当前需求下存在未完成的任务，请完成所有任务再修改需求状态为已完成！");
        }
        return AjaxResult.success();
    }

    /**
     * 删除需求
     */
    @Log(title = "删除需求", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{requirementCodes}")
    public AjaxResult remove(@PathVariable List<String> requirementCodes) {
        return toAjax(mesRequirementService.deleteMesRequirementByRequirementCodes(requirementCodes));
    }

    /**
     * 获取需求编号
     */
    @Log(title = "获取需求编号", businessType = BusinessType.QUERY)
    @PostMapping("/get/requirement-code")
    public AjaxResult getRequirementCodes(@RequestBody MesRequirement mesRequirement) {
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        // 110表示该账号可以看到制造信息部下的所有需求数据，包含电池MES部、组件WMS部...的数据
        // 非110的账号只能看到对应部门下的数据
        Long deptId = null;
        if (sysDept.getDeptId() != 110) {
            deptId = sysDept.getDeptId();
        }
        mesRequirement.setDeptId(deptId);
        return AjaxResult.success(mesRequirementService.getRequirementCodes(mesRequirement));
    }

    /**
     * 获取优化-需求
     */
    @Log(title = "获取需求", businessType = BusinessType.QUERY)
    @GetMapping("/get/requirement/name")
    public AjaxResult getRequirementsByUserName() {
        return AjaxResult.success(mesRequirementService.getRequirementsByUserName());
    }

    /**
     * 申请发布流程
     */
    @Log(title = "申请发布流程", businessType = BusinessType.UPDATE)
    @GetMapping("/generate/{id}")
    public AjaxResult generateReleaseDocument(@PathVariable List<String> id) {
        return AjaxResult.success(mesRequirementService.generateReleaseDocument(id));
    }

    /**
     * 取消需求
     */
    @Log(title = "取消需求", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/requirementAndTask")
    public AjaxResult cancelMesRequirementByRequirementCode(@RequestBody MesRequirement mesRequirement) {
        return toAjax(mesRequirementService.cancelMesRequirementByRequirementCode(mesRequirement));
    }

    /**
     * 查找项目下需求的最小计划开始时间和最大计划结束时间
     */
    @Log(title = "查找计划时间范围", businessType = BusinessType.QUERY)
    @PostMapping("/selectTimeEndpointForProject")
    public AjaxResult selectTimeEndpointForProject(@RequestBody MesRequirement mesRequirement) {
        return AjaxResult.success(mesRequirementService.selectTimeEndpointForProject(mesRequirement));
    }
}
