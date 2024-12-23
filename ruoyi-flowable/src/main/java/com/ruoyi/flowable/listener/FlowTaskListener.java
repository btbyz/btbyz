package com.ruoyi.flowable.listener;

import com.ruoyi.common.config.WeLinkAsyncProperties;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.flowable.domain.dto.WeLinkCommonCardRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务监听器
 *
 * create（创建）:在任务被创建且所有的任务属性设置完成后才触发
 * assignment（指派）：在任务被分配给某个办理人之后触发
 * complete（完成）：在配置了监听器的上一个任务完成时触发
 * delete（删除）：在任务即将被删除前触发。请注意任务由completeTask正常完成时也会触发
 *
 * @author Tony
 * @date 2021/4/20
 */
@Slf4j
@Component
@Data
public class FlowTaskListener implements TaskListener{

    @Override
    public void notify(DelegateTask delegateTask) {
        WeLinkAsyncProperties weLinkAsyncProperties = SpringUtils.getBean(WeLinkAsyncProperties.class);
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        log.info("任务监听器:{}", delegateTask);
        // TODO  获取事件类型 delegateTask.getEventName(),可以通过监听器给任务执行人发送相应的通知消息
        String url = weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendCommonCardUrl();
        WeLinkCommonCardRequest dto = new WeLinkCommonCardRequest();
        dto.setMsgOwner("MES_FLOW");
        dto.setPublicAccID(weLinkAsyncProperties.getWeLinkCommonCardPublicAccID());
        dto.setMsgTitle("MES发布流程申请");
        List<String> list=new ArrayList<>();
        list.add(delegateTask.getAssignee());
        dto.setToUserList(list);
        String nodeType = delegateTask.getTaskDefinitionKey();
        switch (nodeType) {
            case "First_Level_Audit":
                dto.setMsgContent("您有任务编号为"+ delegateTask.getId() +"的任务到达,当前节点发布流程申请一级审核，请及时查看！");
                break;
            case "Second_Level_Audit":
                dto.setMsgContent("您有任务编号为"+ delegateTask.getId() +"的任务到达,当前节点发布流程申请二级审核，请及时查看！");
                break;
            case "Third_Level_Audit":
                dto.setMsgContent("您有任务编号为"+ delegateTask.getId() +"的任务到达,当前节点发布流程申请三级审核，请及时查看！");
                break;
            case "Release_Start":
                dto.setMsgContent("您有任务编号为"+ delegateTask.getId() +"的任务到达,当前节点正式发布开始，请及时查看！");
                break;
            case "Release_End":
                dto.setMsgContent("您有任务编号为"+ delegateTask.getId() +"的任务到达,当前节点正式发布结束，请及时查看！");
                break;
            default:
                dto.setMsgContent(delegateTask.getId() + "发布流程节点ID定义有误，请修改节点ID！");
                break;
        }
        restTemplate.postForObject(url,dto,WeLinkCommonCardRequest.class);
    }

}
