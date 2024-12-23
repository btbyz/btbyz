package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesOaRequirement;
import com.ruoyi.flowable.service.IMesOaRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-05-22
 */
@RestController
@RequestMapping("/mes/oa/requirement")
public class MesOaRequirementController extends BaseController {
    @Autowired
    private IMesOaRequirementService mesOaRequirementService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesOaRequirement mesOaRequirement) {
        startPage();
        List<MesOaRequirement> list = mesOaRequirementService.selectMesOaRequirementList(mesOaRequirement);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesOaRequirement mesOaRequirement)
    {
        List<MesOaRequirement> list = mesOaRequirementService.selectMesOaRequirementList(mesOaRequirement);
        ExcelUtil<MesOaRequirement> util = new ExcelUtil<MesOaRequirement>(MesOaRequirement.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @GetMapping(value = "/{requirementOaId}")
    public AjaxResult getInfo(@PathVariable("requirementOaId") String requirementOaId)
    {
        return success(mesOaRequirementService.selectMesOaRequirementByRequirementOaId(requirementOaId));
    }

    /**
     * 新增【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MesOaRequirement mesOaRequirement) {
        mesOaRequirementService.insertMesOaRequirement(mesOaRequirement);
        return AjaxResult.success();
    }

    /**
     * 修改【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody MesOaRequirement mesOaRequirement) {
        return toAjax(mesOaRequirementService.updateMesOaRequirement(mesOaRequirement));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{requirementOaIds}")
    public AjaxResult remove(@PathVariable List<String> requirementOaIds)
    {
        return toAjax(mesOaRequirementService.deleteMesOaRequirementByRequirementOaIds(requirementOaIds));
    }

    /**
     * 查询登录账号所有未指派OA需求单数据
     * */
    @Log(title = "查询登录账号所有未指派OA需求单数据", businessType = BusinessType.QUERY)
    @PostMapping("/all")
    public AjaxResult getAllRequirement(@RequestBody MesOaRequirement mesOaRequirement) {
        return AjaxResult.success(mesOaRequirementService.getAllRequirement(mesOaRequirement));
    }
}
