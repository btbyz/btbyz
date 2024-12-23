package com.ruoyi.flowable.listener;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.config.WeLinkAsyncProperties;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.flowable.domain.dto.MesDictData;
import com.ruoyi.flowable.domain.dto.WeLinkCommonCardRequest;
import com.ruoyi.flowable.service.IMesDictDataService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.el.FixedValue;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 执行监听器
 * <p>
 * 执行监听器允许在执行过程中执行Java代码。
 * 执行监听器可以捕获事件的类型：
 * 流程实例启动，结束
 * 输出流捕获
 * 获取启动，结束
 * 路由开始，结束
 * 中间事件开始，结束
 * 触发开始事件，触发结束事件
 *
 * @author Tony
 * @date 2022/12/16
 */
@Slf4j
@Component
@Data
public class FlowExecutionListener implements ExecutionListener {
    /**
     * 流程设计器添加的参数
     */
    private Expression param;

    // 节点
    private FixedValue nodeType;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("执行监听器:{}", execution);
        WeLinkAsyncProperties weLinkAsyncProperties =SpringUtils.getBean(WeLinkAsyncProperties.class);
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        ExecutionEntityImpl executionEntityImpl = (ExecutionEntityImpl) execution.getParent();
        IMesDictDataService mesDictDataService = SpringUtils.getBean(IMesDictDataService.class);
        VariableInstanceEntityImpl versionImpl = (VariableInstanceEntityImpl) executionEntityImpl.getVariableInstance("releaseVersion");
        VariableInstanceEntityImpl releaseDescInfoImpl = (VariableInstanceEntityImpl) executionEntityImpl.getVariableInstance("releaseDescInfo");
        VariableInstanceEntityImpl applyDateImpl = (VariableInstanceEntityImpl) executionEntityImpl.getVariableInstance("applyDate");
        String url = weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendCommonCardUrl();
        List<String> list = new ArrayList<>();
        list.add(executionEntityImpl.getStartUserId());
        WeLinkCommonCardRequest dto = new WeLinkCommonCardRequest();
        dto.setMsgOwner("MES_FLOW");
        dto.setPublicAccID(weLinkAsyncProperties.getWeLinkCommonCardPublicAccID());
        dto.setToUserList(list);
        dto.setMsgTitle("MES发布流程申请");
        String nodeType = this.nodeType.getExpressionText();
        switch (nodeType) {
            case "START_EVENT":
                dto.setMsgContent("发布流程申请开始！");
                break;
            case "END_EVENT":
                dto.setMsgContent("发布流程结束，请及时查看！");
                break;
            case "GATEWAY":
                if (executionEntityImpl.getVariableInstance("result").getCachedValue().equals("no")) {
                    dto.setMsgContent("发布流程申请未通过，请及时查看！");
                }
                break;
            case "ACTIVITY":
                MesDictData mesDictData = mesDictDataService.getDictValueByKey(MesDictData.builder().module("ProcessPush").dictCode("StartEvent").build());
                if (mesDictData != null && StringUtils.isNotBlank(mesDictData.getDictValue())) {
                    List<String> userList = Arrays.asList(mesDictData.getDictValue().split(StringPool.COMMA));
                    dto.setToUserList(userList);
                }
                dto.setMsgContent("发布版本:" + versionImpl.getCachedValue() + "\n" +
                        "计划发布时间:" + applyDateImpl.getCachedValue()+ "\n" +
                        "实际发布时间:"+ DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date())+ "\n" +
                        "发布内容:" + "\n" +
                        releaseDescInfoImpl.getCachedValue().toString()
                        .replace("<p>","")
                        .replace("</p>","")
                        .replace("<br />","\n")
                        .replace("<br/>","\n")
                );
                break;
            default:
                dto.setMsgContent("发布流程参数配置错误！");
                break;
        }
        restTemplate.postForObject(url, dto, WeLinkCommonCardRequest.class);
    }
}
