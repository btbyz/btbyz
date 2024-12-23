package com.ruoyi.flowable.listener;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.config.WeLinkAsyncProperties;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.flowable.domain.dto.MesDictData;
import com.ruoyi.flowable.domain.dto.WeLinkSendRequest;
import com.ruoyi.flowable.service.IMesDictDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.el.FixedValue;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class FlowStartTaskListener implements ExecutionListener {

    // 节点类型
    private FixedValue nodeType;

    @Override
    public void notify(DelegateExecution delegateExecution) {
        try {
            WeLinkSendRequest request = new WeLinkSendRequest();
            WeLinkAsyncProperties weLinkAsyncProperties = SpringUtils.getBean(WeLinkAsyncProperties.class);
            RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
            IMesDictDataService mesDictDataService = SpringUtils.getBean(IMesDictDataService.class);
            request.setRobotUrl(weLinkAsyncProperties.getMesSchemaWeLinkRobotUrl());
            request.setIsAtFlag(true);
            request.setContent("发布流程已开始!");
            MesDictData dictData = mesDictDataService.getDictValueByKey(MesDictData.builder().module("ProcessPush").dictCode("StartEvent").build());
            request.setAtUserList(dictData !=null && StringUtils.isNotBlank(dictData.getDictValue()) ? Arrays.stream(dictData.getDictValue().split(StringPool.COMMA)).collect(Collectors.toList()) : new ArrayList<>());
            restTemplate.postForObject(weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendRobotMsgUrl(), request, Object.class);
        } catch (Exception e) {
            log.error("sendMessageTaskToIndividualTask error:", e);
        }
    }
}
