package com.ruoyi.quartz.task;

import com.ruoyi.flowable.service.IMesDimTdDateService;
import com.ruoyi.flowable.service.IMesWorkTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * wafer验证任务定时从实时表移到历史表
 */
@Component("sendTimeCardMessageTask")
public class SendTimeCardMessageTask {

    @Resource
    private IMesWorkTimeService mesWorkTimeService;

    @Resource
    private IMesDimTdDateService mesDimTdDateService;

    private static final Logger logger = LoggerFactory.getLogger(SendTimeCardMessageTask.class);
    public void sendMessageTaskToIndividual() {
        long startTime = System.currentTimeMillis();
        logger.warn("sendTimeCardMessageTask.sendMessageTaskToIndividual start");
        mesWorkTimeService.sendMessageTaskToIndividual();
        logger.warn("sendTimeCardMessageTask.sendMessageTaskToIndividual end.耗时：{}毫秒", System.currentTimeMillis() - startTime);
    }

    public void sendRobotMsg() {
        long startTime = System.currentTimeMillis();
        logger.warn("sendTimeCardMessageTask.sendRobotMsg start");
        mesWorkTimeService.sendRobotMsg();
        logger.warn("sendTimeCardMessageTask.sendRobotMsg end.耗时：{}毫秒", System.currentTimeMillis() - startTime);
    }

    public void sendMessageTaskToManage() {
        long startTime = System.currentTimeMillis();
        logger.warn("sendTimeCardMessageTask.sendMessageTaskToManage start");
        mesWorkTimeService.sendMessageTaskToManage();
        logger.warn("sendTimeCardMessageTask.sendMessageTaskToManage end.耗时：{}毫秒", System.currentTimeMillis() - startTime);
    }

    public void generateWorkTime() {
        long startTime = System.currentTimeMillis();
        logger.warn("sendTimeCardMessageTask.generateWorkTime start");
        mesDimTdDateService.generateCalendar();
        logger.warn("sendTimeCardMessageTask.generateWorkTime end.耗时：{}毫秒", System.currentTimeMillis() - startTime);
    }
}