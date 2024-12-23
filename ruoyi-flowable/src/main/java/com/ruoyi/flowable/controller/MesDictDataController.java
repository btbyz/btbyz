package com.ruoyi.flowable.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.flowable.domain.dto.MesDictData;
import com.ruoyi.flowable.service.IMesDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2024-04-07
 */
@RestController
@RequestMapping("/mes/dict-data")
public class MesDictDataController extends BaseController {

    @Autowired
    private IMesDictDataService mesDictDataService;

    /**
     * 查询【请填写功能名称】列表
     */
    @GetMapping("/list")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.QUERY)
    public TableDataInfo list(MesDictData mesDictData) {
        startPage();
        List<MesDictData> list = mesDictDataService.selectMesDictDataList(mesDictData);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(@RequestBody MesDictData mesDictData) {
        List<MesDictData> list = mesDictDataService.selectMesDictDataList(mesDictData);
        return AjaxResult.success(list);
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.QUERY)
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(mesDictDataService.selectMesDictDataById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping("/insert")
    public AjaxResult add(@RequestBody MesDictData mesDictData) {
        return toAjax(mesDictDataService.insertMesDictData(mesDictData));
    }

    /**
     * 修改【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult edit(@RequestBody MesDictData mesDictData) {
        return toAjax(mesDictDataService.updateMesDictData(mesDictData));
    }

    /**
     * 删除【请填写功能名称】
     */
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(mesDictDataService.deleteMesDictDataByIds(ids));
    }

    /**
     * 通过module和dict_code查询对应值
     * */
    @Log(title = "通过module和dict_code查询对应值", businessType = BusinessType.QUERY)
    @PostMapping("/module/dict-code")
    public AjaxResult getDictValueByKey(@RequestBody MesDictData mesDictData) {
        return AjaxResult.success(mesDictDataService.getDictValueByKey(mesDictData));
    }

    /**
     * 通过module和dict_code和部门查询对应值
     * */
    @Log(title = "通过module和dict_code和部门查询对应值", businessType = BusinessType.QUERY)
    @PostMapping("/module/dict-code/dept-id")
    public AjaxResult getDictValueByKeyDept(@RequestBody MesDictData mesDictData) {
        return AjaxResult.success(mesDictDataService.getDictValueByKeyDept(mesDictData));
    }

    /**
     * 获取去重module、dictCode、remark
     * */
    @Log(title = "获取去重module、dictCode、remark", businessType = BusinessType.QUERY)
    @GetMapping("/distinct")
    public AjaxResult distinct() {
        return AjaxResult.success(mesDictDataService.distinct());
    }

}
