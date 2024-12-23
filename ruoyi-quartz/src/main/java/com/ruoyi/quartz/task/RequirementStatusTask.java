package com.ruoyi.quartz.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.flowable.common.enums.DeliveryStatusEnum;
import com.ruoyi.flowable.common.enums.RequirementStatusEnum;
import com.ruoyi.flowable.domain.dto.MesProject;
import com.ruoyi.flowable.domain.dto.MesRequirement;
import com.ruoyi.flowable.domain.dto.MesRequirementTask;
import com.ruoyi.flowable.service.IMesProjectService;
import com.ruoyi.flowable.service.IMesRequirementService;
import com.ruoyi.flowable.service.IMesRequirementTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("requirementStatusTask")
public class RequirementStatusTask {

    private final Logger log = LoggerFactory.getLogger(RequirementStatusTask.class);

    @Autowired
    private IMesRequirementService mesRequirementService;
    @Autowired
    private IMesRequirementTaskService mesRequirementTaskService;

    @Autowired
    private IMesProjectService mesProjectService;

    public void requirementStatusTask() {
        Date now = new Date();
        Date completeDate = DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(now, -1))) ;
        MesRequirement mesRequirement = new MesRequirement();
        mesRequirement.setEndTime(completeDate);
        try {
            List<MesRequirement> requirements = mesRequirementService.getRequirementList(mesRequirement);
            if (CollectionUtils.isNotEmpty(requirements)) {
                requirements.forEach(value -> {
                    if (!value.getStatus().equals("Canceled")) {
                        value.setStatus("Completed"); //完成
                    }
                });
                mesRequirementService.updateBatchMesRequirementById(requirements);
                List<String> requirementCodes = requirements.stream().filter(item -> !item.getStatus().equals("Canceled"))
                        .map(MesRequirement::getRequirementCode)
                        .collect(Collectors.toList());
                mesRequirementTaskService.updateBatchMesRequirementTaskStatus(requirementCodes, "Completed");
            }
        } catch (Exception e) {
            log.error("RequirementStatusTask error:", e);
        }
    }

    public void requirementStatusChangeTask() {
        Date beginDate = DateUtils.parseDate(DateUtils.getDate()) ;
        MesRequirement mesRequirement = new MesRequirement();
        mesRequirement.setBeginTime(beginDate);
        try {
            List<MesRequirement> requirements = mesRequirementService.getRequirementList(mesRequirement);
            if (CollectionUtils.isNotEmpty(requirements)) {
                requirements.forEach(value -> {
                    if (!value.getStatus().equals("Canceled")) {
                        value.setStatus("InProgress"); //进行中
                    }
                });
                mesRequirementService.updateBatchMesRequirementById(requirements);
                List<String> requirementCodes = requirements.stream()
                        .filter(item -> !item.getStatus().equals("Canceled"))
                        .map(MesRequirement::getRequirementCode)
                        .collect(Collectors.toList());
                mesRequirementTaskService.updateBatchMesRequirementTaskStatus(requirementCodes, "InProgress");
            }
        } catch (Exception e) {
            log.error("requirementStatusChangeTask error:", e);
        }
    }

    @Transactional
    public void deliveryStatusChangeTask() {
        Date now = new Date();
        Date completeDate = DateUtils.parseDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(now, -1))) ;
        try {
            // 更新项目状态-----更新超出计划开始时间状态仍为未开始的项目交付状态为 超时未开始
            List<MesProject> nonStartProjects = mesProjectService.lambdaQuery()
                    .eq(MesProject::getBeginTime,completeDate)
                    .eq(MesProject::getStatus, RequirementStatusEnum.UN_START.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(nonStartProjects)) {
                nonStartProjects.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.NON_START.getCode());
                });
                mesProjectService.updateBatchById(nonStartProjects);
            }
            // 更新项目状态----更新超出计划完成时间状态仍为进行中或未开始的项目交付状态为延期
            List<MesProject> overDueProjects = mesProjectService.lambdaQuery()
                    .eq(MesProject::getEndTime,completeDate)
                    .ne(MesProject::getStatus,RequirementStatusEnum.COMPLETED.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(overDueProjects)) {
                overDueProjects.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.OVER_DUE.getCode());
                });
                mesProjectService.updateBatchById(overDueProjects);
            }
            // 更新需求状态-----更新超出计划开始时间状态仍为未开始的需求交付状态为 超时未开始
            List<MesRequirement> nonStartRequirements = mesRequirementService.lambdaQuery()
                    .eq(MesRequirement::getBeginTime,completeDate)
                    .eq(MesRequirement::getStatus, RequirementStatusEnum.UN_START.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(nonStartRequirements)) {
                nonStartRequirements.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.NON_START.getCode());
                });
                mesRequirementService.updateBatchMesRequirementById(nonStartRequirements);
            }
            // 更新需求状态-----更新超出计划完成时间状态仍为进行中或未开始的需求交付状态为延期
            List<MesRequirement> overDueRequirements = mesRequirementService.lambdaQuery()
                    .eq(MesRequirement::getEndTime,completeDate)
                    .ne(MesRequirement::getStatus,RequirementStatusEnum.COMPLETED.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(overDueRequirements)) {
                overDueRequirements.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.OVER_DUE.getCode());
                });
                mesRequirementService.updateBatchMesRequirementById(overDueRequirements);
            }
            // 更新任务状态------更新超出计划开始时间状态仍为未开始的任务交付状态为 超时未开始
            List<MesRequirement> nonStartRequirementTasks = mesRequirementService.lambdaQuery()
                    .eq(MesRequirement::getBeginTime,completeDate)
                    .eq(MesRequirement::getStatus, RequirementStatusEnum.UN_START.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(nonStartRequirementTasks)) {
                nonStartRequirementTasks.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.NON_START.getCode());
                });
                mesRequirementService.updateBatchMesRequirementById(nonStartRequirementTasks);
            }
            // 更新任务状态------更新超出计划完成时间状态仍为进行中或未开始的任务交付状态为延期
            List<MesRequirementTask> overDueRequirementTasks = mesRequirementTaskService.lambdaQuery()
                    .eq(MesRequirementTask::getEndTime,completeDate)
                    .ne(MesRequirementTask::getStatus,RequirementStatusEnum.COMPLETED.getCode())
                    .list();
            if (CollectionUtils.isNotEmpty(overDueRequirementTasks)) {
                overDueRequirementTasks.forEach(value -> {
                    value.setDeliveryStatus(DeliveryStatusEnum.OVER_DUE.getCode()); //完成
                });
                mesRequirementTaskService.updateBatchById(overDueRequirementTasks);
            }
        } catch (Exception e) {
            log.error("DeliveryStatusChangeTask error:", e);
        }
    }
}
