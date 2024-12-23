package com.ruoyi.flowable.controller;

import com.ruoyi.flowable.config.MesProperties;
import com.ruoyi.flowable.domain.dto.MesProjectRecords;
import com.ruoyi.flowable.service.IMesProjectRecordsService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 需求项目记录Controller
 * 
 * @author wangxin
 * @date 2024-03-11
 */
@RestController
@RequestMapping("/mes/project_records")
public class MesProjectRecordsController extends BaseController {

    @Autowired
    private IMesProjectRecordsService mesProjectRecordsService;

    @Autowired
    private MesProperties mesProperties;
    /**
     * 查询需求项目记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MesProjectRecords mesProjectRecords) {
        startPage();
        List<MesProjectRecords> list = mesProjectRecordsService.selectMesProjectRecordsList(mesProjectRecords);
        return getDataTable(list);
    }

    /**
     * 导出需求项目记录列表
     *
     */
    @Log(title = "需求项目记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(@RequestBody MesProjectRecords mesProjectRecords) {
       List<MesProjectRecords> list = mesProjectRecordsService.selectMesProjectRecordsList(mesProjectRecords);
       return AjaxResult.success(list);
    }

    /**
     * 获取需求项目记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(mesProjectRecordsService.selectMesProjectRecordsById(id));
    }

    /**
     * 新增需求项目记录
     */
    @Log(title = "需求项目记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MesProjectRecords mesProjectRecords) {
        return toAjax(mesProjectRecordsService.insertMesProjectRecords(mesProjectRecords));
    }

    /**
     * 修改需求项目记录
     */
    @Log(title = "需求项目记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MesProjectRecords mesProjectRecords) {
        return toAjax(mesProjectRecordsService.updateMesProjectRecords(mesProjectRecords));
    }

    /**
     * 删除需求项目记录
     */
    @Log(title = "需求项目记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String[] id) {
        return toAjax(mesProjectRecordsService.deleteMesProjectRecordsByIds(id));
    }

    /**
     * 生成发布文档
     * */
    @Log(title = "生成发布文档", businessType = BusinessType.UPDATE)
    @PostMapping("/generate/{id}")
    public AjaxResult generateReleaseDocument(@PathVariable Long[] id) {
        return toAjax(mesProjectRecordsService.generateReleaseDocument(id));
    }

}
