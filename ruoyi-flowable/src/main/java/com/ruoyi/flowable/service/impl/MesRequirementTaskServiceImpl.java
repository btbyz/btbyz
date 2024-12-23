package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.flowable.common.enums.DeliveryStatusEnum;
import com.ruoyi.flowable.common.enums.RequirementStatusEnum;
import com.ruoyi.flowable.domain.dto.MesDictData;
import com.ruoyi.flowable.domain.dto.MesRequirement;
import com.ruoyi.flowable.domain.dto.MesRequirementTask;
import com.ruoyi.flowable.domain.dto.MesWorkTime;
import com.ruoyi.flowable.mapper.MesRequirementTaskMapper;
import com.ruoyi.flowable.service.*;
import com.ruoyi.flowable.util.RequirementUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-04-10
 */
@Service
public class MesRequirementTaskServiceImpl extends ServiceImpl<MesRequirementTaskMapper, MesRequirementTask> implements IMesRequirementTaskService {
    @Autowired
    private MesRequirementTaskMapper mesRequirementTaskMapper;
    @Autowired
    private IMesReleaseLogService releaseLogService;
    @Autowired
    private IMesRequirementService mesRequirementService;
    @Autowired
    private IMesRequirementTaskService mesRequirementTaskService;
    @Autowired
    private IMesProjectService projectService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IMesDictDataService mesDictDataService;
    @Autowired
    private IMesWorkTimeService mesWorkTimeService;

    private static final String TASK_CODE_PREFIX = "MITT";
    /**
     * 查询【请填写功能名称】
     *
     * @param taskCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesRequirementTask selectMesRequirementTaskByTaskCode(String taskCode) {
        MesRequirementTask task = this.getById(taskCode);
        MesRequirementTask mesRequirementTask = mesRequirementTaskService.selectMesRequirementTaskList( MesRequirementTask.builder().taskCode(taskCode).deptId(task.getDeptId()).build()).get(0);
//        mesRequirementTask.setAssignPerson(mesRequirementTask.getDevelopers());
        return mesRequirementTask;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesRequirementTask> selectMesRequirementTaskList(MesRequirementTask mesRequirementTask)
    {
        List<Long> list = new ArrayList<>();
        list.add(mesRequirementTask.getDeptId());
        list.add(110L);
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        if (sysDept.getDeptId() == 110) {
            list.add(114L);
            list.add(115L);
            list.add(116L);
        }
        mesRequirementTask.setDeptList(list);
        return mesRequirementTaskMapper.selectMesRequirementTaskList(mesRequirementTask);
    }

    /**
     * 查询timecard可用的任务列表
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    @Override
    public List<MesRequirementTask> selectMesRequirementTaskListForTimeCard(MesRequirementTask mesRequirementTask)
    {
        List<MesRequirementTask> mesRequirementTaskList = new ArrayList<>();
        mesRequirementTask.setActualEndTime(DateUtils.addDays(mesRequirementTask.getActualBeginTime(), -3));
        // 优化类工作加上 公有任务
        if (StringUtils.isNotEmpty(mesRequirementTask.getAttribute())  && mesRequirementTask.getAttribute().equals("Optimization")) {
            MesDictData mesDictData = mesDictDataService.lambdaQuery().eq(MesDictData::getModule, "workTime")
                    .eq(MesDictData::getDictCode, "common_task").one();
            if (mesDictData != null) {
                List<String> commonTasks = Arrays.asList(mesDictData.getDictValue().split(","));
                for (int i = 0; i < commonTasks.size(); i++) {
                    MesRequirementTask task = new MesRequirementTask();
                    task.setTaskName(commonTasks.get(i));
                    task.setTaskCode(RequirementUtils.getRequirementCode("MICT", i + 1));
                    mesRequirementTaskList.add(task);
                }
            }
        }
        mesRequirementTaskList.addAll(mesRequirementTaskMapper.selectMesRequirementTaskListForTimeCard(mesRequirementTask));
        return mesRequirementTaskList;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMesRequirementTask(MesRequirementTask mesRequirementTask)
    {
        MesRequirementTask requirementTask = mesRequirementTaskMapper.selectOneByRequirementCodeDesc();
        String taskCode;
        if (requirementTask != null) {
            String yearCode = DateUtils.dateTimeNow("yyyy").substring(2);
            if (requirementTask.getTaskCode().contains(TASK_CODE_PREFIX + yearCode)) {
                int num = Integer.parseInt(requirementTask.getTaskCode().substring(requirementTask.getTaskCode().length() - 5));
                taskCode = RequirementUtils.getRequirementCode(TASK_CODE_PREFIX, num);
            } else {
                taskCode = RequirementUtils.getRequirementCode(TASK_CODE_PREFIX, 0);
            }
        } else {
            taskCode = RequirementUtils.getRequirementCode(TASK_CODE_PREFIX, 0);
        }
        mesRequirementTask.setTaskCode(taskCode);
        return mesRequirementTaskMapper.insert(mesRequirementTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assignTask(MesRequirementTask mesRequirementTask) {
        FabAssertUtils.isNotBlank(mesRequirementTask.getTaskName(), BaseErrorCodeEnum.NULL_ERROR, "任务名称不能为空！");
        FabAssertUtils.isNotBlank(mesRequirementTask.getAssignPerson(), BaseErrorCodeEnum.NULL_ERROR, "指派人员不能为空！");
        MesRequirementTask task = new MesRequirementTask();
        task.setTaskName(mesRequirementTask.getRequirementName() + '-' + mesRequirementTask.getTaskName());
        SysUser sysUserF = SecurityUtils.getLoginUser().getUser();
        if (sysUserF.getDeptId() != 110) {
            task.setDeptId(sysUserF.getDeptId());
        }
        int count = mesRequirementTaskMapper.countByName(task);
        if (count > 0) {
            FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "任务名称已存在,请更改任务名称再提交!");
        }
        SysUser sysUer = sysUserService.selectUserByUserName(mesRequirementTask.getAssignPerson());
        FabAssertUtils.isNotNull(sysUer, BaseErrorCodeEnum.NULL_ERROR, "未查询到指派人员信息!");
        String developers = sysUer.getUserName();
        Long deptId = sysUer.getDept().getDeptId();
        mesRequirementTask.setDevelopers(developers);
        mesRequirementTask.setDeptId(deptId);
        mesRequirementTask.setTaskName(mesRequirementTask.getRequirementName() + '-' + mesRequirementTask.getTaskName());
        mesRequirementTask.setDeliveryStatus(mesRequirementTaskService.getDeliveryStatus(mesRequirementTask));
        int num = mesRequirementTaskService.insertMesRequirementTask(mesRequirementTask);
        if (num == 1) {
            // 任务改变引起需求变化
            mesRequirementTaskService.changeRequirementStatus(mesRequirementTask);
            // 需求变化引起项目变化
            MesRequirement mesRequirement = mesRequirementService.getById(mesRequirementTask.getRequirementCode());
            mesRequirementService.changeProjectStatus(mesRequirement);
//            if (mesRequirement != null && StringUtils.isNotEmpty(mesRequirement.getProjectCode())) {
//                projectService.updateProjectStatus(mesRequirement.getProjectCode());
//            }
            return 1;
        }
        return 0;
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mesRequirementTask 【请填写功能名称】
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMesRequirementTask(MesRequirementTask mesRequirementTask) {
        if (mesRequirementTask.getActualEndTime() != null) {
            MesWorkTime mesWorkTime = mesWorkTimeService.selectMaxWorkTime(MesWorkTime.builder().workCode(mesRequirementTask.getTaskCode()).build());
            int diffDays = 0;
            // 获取最新的工时记录填写时间与任务实际结束时间的差值
            if (mesWorkTime != null && mesWorkTime.getMaxWorkTime() != null) {
                diffDays = (int) ((mesWorkTime.getMaxWorkTime().getTime() - mesRequirementTask.getActualEndTime().getTime()) / (1000 * 3600 * 24));
            }
            FabAssertUtils.isTrue(diffDays <= 3,BaseErrorCodeEnum.PARAM_ERROR, "仅能将实际结束时间最多修改至该任务最新一条工时记录的前三天！");
        }
        SysUser sysUer = sysUserService.selectUserByUserName(mesRequirementTask.getAssignPerson());
        if (sysUer != null) {
            String developers = sysUer.getUserName();
            Long deptId = sysUer.getDept().getDeptId();
            mesRequirementTask.setDevelopers(developers);
            mesRequirementTask.setDeptId(deptId);
        }
        if (!mesRequirementTask.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())) {
            if (mesRequirementTask.getStatus().equals(RequirementStatusEnum.UN_START.getCode())) {
                mesRequirementTask.setActualBeginTime(null);
            }
            mesRequirementTask.setActualEndTime(null);
        }
        mesRequirementTask.setDeliveryStatus(mesRequirementTaskService.getDeliveryStatus(mesRequirementTask));
        int num = mesRequirementTaskMapper.updateById(mesRequirementTask);
        // 任务变化引起需求变化
        mesRequirementTaskService.changeRequirementStatus(mesRequirementTask);
        // 需求变化引起项目变化
        MesRequirement mesRequirement = mesRequirementService.getById(mesRequirementTask.getRequirementCode());
        mesRequirementService.changeProjectStatus(mesRequirement);
        return num;
    }

    @Override
    public boolean updateBatchMesRequirementTaskStatus(List<String> requirementCodes, String status) {
        UpdateWrapper<MesRequirementTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(MesRequirementTask::getRequirementCode, requirementCodes);
        updateWrapper.lambda().set(MesRequirementTask::getStatus, status);
        updateWrapper.lambda().set(MesRequirementTask::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper);
    }

    @Override
    public boolean updateBatchMesRequirementTaskStatus(List<String> requirementCodes, String status, Date beginTime, Date endTime) {
        UpdateWrapper<MesRequirementTask> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(MesRequirementTask::getRequirementCode, requirementCodes);
        updateWrapper.lambda().set(MesRequirementTask::getStatus, status);
        updateWrapper.lambda().set(MesRequirementTask::getBeginTime, beginTime);
        updateWrapper.lambda().set(MesRequirementTask::getEndTime, endTime);
        updateWrapper.lambda().set(MesRequirementTask::getUpdateTime, LocalDateTime.now());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param taskCodes 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMesRequirementTaskByTaskCodes(List<String> taskCodes)
    {
        MesRequirementTask mesRequirementTask = mesRequirementTaskService.selectMesRequirementTaskByTaskCode(taskCodes.get(0));
        int num = mesRequirementTaskMapper.deleteBatchIds(taskCodes);
        // 任务变化引起需求变化
        mesRequirementTaskService.changeRequirementStatus(mesRequirementTask);
        // 需求变化引起项目变化
        MesRequirement mesRequirement = mesRequirementService.getById(mesRequirementTask.getRequirementCode());
        mesRequirementService.changeProjectStatus(mesRequirement);
        return num;
    }

    /**
     * 根据需求code批量删除任务列表
     *
     * @param requirementCodes 需要删除的需求code
     */
    @Override
    public void deleteMesRequirementTaskByRequirementCode(List<String> requirementCodes){
        mesRequirementTaskMapper.deleteByRequirementCode(requirementCodes);
    }

    @Override
    public List<MesRequirementTask> getTaskCodes(Long deptId) {
        return mesRequirementTaskMapper.getTaskCodes(deptId);
    }

    @Override
    public MesRequirementTask selectTimeEndpoint(MesRequirementTask mesRequirementTask) {
        return mesRequirementTaskMapper.selectTimeEndpoint(mesRequirementTask);
    }

    /**
     * 任务改变引起需求状态和需求交付状态变动
     * @param mesRequirementTask
     */
    @Override
    public void changeRequirementStatus(MesRequirementTask mesRequirementTask) {
        MesRequirementTask timeEndpoint = mesRequirementTaskService.selectTimeEndpoint(mesRequirementTask);
        if (StringUtils.isNotEmpty(mesRequirementTask.getRequirementCode())) {
            List<String> tempList = new ArrayList<>();
            tempList.add(mesRequirementTask.getDevelopers());
            MesRequirement requirement = mesRequirementService.selectMesRequirementByRequirementCode(mesRequirementTask.getRequirementCode());
            if (requirement != null) {
                // 指派人员变更
                if (StringUtils.isNotEmpty(requirement.getAssignPerson())) {
                    List<String> requirementAssignPersons = Arrays.asList(requirement.getAssignPerson().split(","));
                    tempList.addAll(requirementAssignPersons);
                }
                HashSet<String> set = new HashSet<>(tempList);
                List<String> assignPersons = new ArrayList<>(set);
                requirement.setAssignPersons(assignPersons);
                // 任务不存在实际开始和实际结束时间 状态设为未开始，
                // 任务存在最早实际开始和最晚实际结束时间 需求状态不是已完成则设为进行中，是已完成且当前任务状态也为已完成则需求状态是已完成(已完成的需求不允许指派任务，只可能修改)
                // 任务存在实际最早开始时间，不存在最晚实际结束时间，需求状态设为进行中
                String status = RequirementStatusEnum.COMPLETED.getCode();
                String deliveryStatus = null;
                Date actualEndTime = null;
                if (timeEndpoint == null || timeEndpoint.getMinActualBeginTime() == null && timeEndpoint.getMaxActualEndTime() == null) {
                    status = RequirementStatusEnum.UN_START.getCode();
                    deliveryStatus = requirement.getBeginTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.NON_START.getCode() : null;
                } else if (timeEndpoint.getMinActualBeginTime() != null && timeEndpoint.getMaxActualEndTime() == null || !Objects.equals(requirement.getStatus(), RequirementStatusEnum.COMPLETED.getCode())) {
                    status = RequirementStatusEnum.IN_PROGRESS.getCode();
                    deliveryStatus = requirement.getEndTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.ON_GOING.getCode();
                } else if (timeEndpoint.getMinActualBeginTime() != null && Objects.equals(requirement.getStatus(), RequirementStatusEnum.COMPLETED.getCode()) && mesRequirementTask.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())) {
                    deliveryStatus = requirement.getEndTime().before(timeEndpoint.getMaxActualEndTime()) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.COMPLETED.getCode();
                    actualEndTime = timeEndpoint.getMaxActualEndTime();
                }
                requirement.setStatus(status);
                requirement.setDeliveryStatus(deliveryStatus);
                requirement.setActualBeginTime(timeEndpoint == null ? null :timeEndpoint.getMinActualBeginTime());
                requirement.setActualEndTime(actualEndTime);
                mesRequirementService.updateById(requirement);
            }
        }
    }

    @Override
    public String getDeliveryStatus(MesRequirementTask mesRequirementTask) {
        String deliveryStatus = null;
        if (mesRequirementTask.getStatus().equals(RequirementStatusEnum.UN_START.getCode()) && mesRequirementTask.getBeginTime().before(DateUtils.parseDate(DateUtils.getDate()))) {
            deliveryStatus = DeliveryStatusEnum.NON_START.getCode();
        } else if (mesRequirementTask.getStatus().equals(RequirementStatusEnum.IN_PROGRESS.getCode())) {
            deliveryStatus = mesRequirementTask.getEndTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.OVER_DUE.getCode() :DeliveryStatusEnum.ON_GOING.getCode();
        } else if (mesRequirementTask.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())){
            deliveryStatus = mesRequirementTask.getEndTime().before(mesRequirementTask.getActualEndTime()) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.COMPLETED.getCode();
        }
        return deliveryStatus;
    }
}