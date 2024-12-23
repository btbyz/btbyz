package com.ruoyi.flowable.open;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.flowable.domain.dto.MesOaRequirement;
import com.ruoyi.flowable.service.IMesOaRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oa")
public class OaOpenApiController extends BaseController {

    @Autowired
    private IMesOaRequirementService mesOaRequirementService;

    @PostMapping("/mes/requirement")
    @Log(title = "同步OA需求", businessType = BusinessType.INSERT)
    public AjaxResult receiveOaData(@RequestBody MesOaRequirement mesOaRequirement) {
        mesOaRequirementService.insertMesOaRequirement(mesOaRequirement);
        return AjaxResult.success();
    }

}
