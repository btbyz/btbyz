package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.flowable.domain.dto.MesProject;
import com.ruoyi.flowable.service.IMesProjectService;
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
@RequestMapping("/mes/project")
public class MesProjectController extends BaseController
{
    @Autowired
    private IMesProjectService mesProjectService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesProject mesProject)
    {
        startPage();
        List<MesProject> list = mesProjectService.selectMesProjectList(mesProject);
        return getDataTable(list);
    }

    /**
     * 导出项目
     */
    @Log(title = "导出项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MesProject mesProject)
    {
        List<MesProject> list = mesProjectService.selectMesProjectList(mesProject);
        ExcelUtil<MesProject> util = new ExcelUtil<MesProject>(MesProject.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取项目
     */
    @GetMapping(value = "/get/{projectCode}")
    public AjaxResult getInfo(@PathVariable("projectCode") String projectCode)
    {
        return success(mesProjectService.selectMesProjectByProjectCode(projectCode));
    }

    /**
     * 新增或修改项目
     */
    @Log(title = "新增或修改项目", businessType = BusinessType.INSERT)
    @PostMapping("/save-or-update")
    public AjaxResult add(@RequestBody MesProject mesProject)
    {
        return toAjax(mesProjectService.saveOrUpdateMesProject(mesProject));
    }

    /**
     * 删除项目
     */
    @Log(title = "删除项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{projectCodes}")
    public AjaxResult remove(@PathVariable List<String> projectCodes)
    {
        return toAjax(mesProjectService.deleteMesProjectByIds(projectCodes));
    }

    /**
     *
     * */
    @Log(title = "获取项目列表", businessType = BusinessType.OTHER)
    @GetMapping("/project/code/list")
    public AjaxResult getProjectCodeList() {
        return AjaxResult.success(mesProjectService.getProjectCodeList());
    }


}