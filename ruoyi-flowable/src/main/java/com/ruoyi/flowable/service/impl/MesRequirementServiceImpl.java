package com.ruoyi.flowable.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.flowable.common.enums.DeliveryStatusEnum;
import com.ruoyi.flowable.common.enums.RequirementStatusEnum;
import com.ruoyi.flowable.domain.dto.*;
import com.ruoyi.flowable.domain.vo.ExportRequirementVo;
import com.ruoyi.flowable.domain.vo.MesRequirementVo;
import com.ruoyi.flowable.mapper.MesRequirementMapper;
import com.ruoyi.flowable.service.*;
import com.ruoyi.flowable.util.RequirementUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-04-08
 */

@Service
public class MesRequirementServiceImpl extends ServiceImpl<MesRequirementMapper, MesRequirement> implements IMesRequirementService {

    @Autowired
    private MesRequirementMapper mesRequirementMapper;
    @Autowired
    private IMesReleaseLogService releaseLogService;
    @Autowired
    private IMesRequirementService mesRequirementService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private IMesRequirementTaskService mesRequirementTaskService;
    @Autowired
    private IMesWorkTimeService mesWorkTimeService;
    @Autowired
    private IMesOaRequirementService mesOaRequirementService;
    @Autowired
    private IMesProjectService mesProjectService;

    private static final String REQUIREMENT_CODE_PREFIX = "MITE";
    private static final String PROJECT_CODE_PREFIX = "MITP";

    /**
     * 查询【请填写功能名称】
     *
     * @param requirementCode 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesRequirement selectMesRequirementByRequirementCode(String requirementCode)
    {
        MesRequirement mesRequirement = this.getById(requirementCode);
        if (mesRequirement != null) {
            String assignPerson = mesRequirement.getAssignPerson();
            if (StringUtils.isNotEmpty(assignPerson)) {
                mesRequirement.setAssignPersons(Arrays.asList(assignPerson.split(",")));
            }
        }
        return mesRequirement;
    }

    /**
     * 获取需求明细数据 包括需求明细+任务明细数据
     * */
    @Override
    public MesRequirement getRequirementsDetails(MesRequirement mesRequirement){
        MesRequirement mesRequirementDetail = this.getById(mesRequirement.getRequirementCode());
        if (mesRequirementDetail != null) {
            String assignPerson = mesRequirementDetail.getAssignPerson();
            if (StringUtils.isNotEmpty(assignPerson)) {
                mesRequirementDetail.setAssignPersons(Arrays.asList(assignPerson.split(",")));
            }
            // 查询需求下的任务编码合集
            List<String> codeList = mesRequirementTaskService.lambdaQuery()
                    .eq(MesRequirementTask::getRequirementCode,mesRequirement.getRequirementCode()).list().stream()
                    .map(MesRequirementTask::getTaskCode)
                    .collect(Collectors.toList());
            // 查询任务明细
            List<MesRequirementTask> requirementTasks = new ArrayList<>();
            codeList.forEach(item -> {
                MesRequirementTask mesRequirementTask = mesRequirementTaskService.selectMesRequirementTaskByTaskCode(item);
                mesRequirementTask.setTaskTime(mesWorkTimeService.lambdaQuery().eq(MesWorkTime::getWorkCode,item).list().stream()
                        .mapToDouble(obj -> Double.parseDouble(obj.getWorkHour())).sum());
                requirementTasks.add(mesRequirementTask);
            });
            mesRequirementDetail.setMesRequirementTaskList(requirementTasks);
            // 计算需求和任务消耗工时
            codeList.add(mesRequirement.getRequirementCode());
            double totalTime = mesWorkTimeService.lambdaQuery().in(MesWorkTime::getWorkCode,codeList).list().stream()
                    .mapToDouble(obj -> Double.parseDouble(obj.getWorkHour())).sum();
            mesRequirementDetail.setTotalTime(totalTime);
        }
        return mesRequirementDetail;
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesRequirement> selectMesRequirementList(MesRequirement mesRequirement)
    {
        List<Long> list = new ArrayList<>(Arrays.asList(mesRequirement.getDeptId(), 110L));
        try {
            SysDept sysDept = Objects.requireNonNull(SecurityUtils.getLoginUser()).getUser().getDept();
            if (sysDept.getDeptId() == 110L) {
                list.addAll(Arrays.asList(114L, 115L, 116L));
            }
            mesRequirement.setDeptList(list);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        List<MesRequirement> requirements = mesRequirementMapper.selectMesRequirementList(mesRequirement);
        if (CollectionUtils.isNotEmpty(requirements)) {
            requirements.forEach(requirement -> {
                requirement.setVersionFlag(false);
                if (StringUtils.isNotEmpty(requirement.getReleaseVersion())) {
                    requirement.setVersionFlag(true);
                }
            });
        }
        return requirements;
    }

    @Override
    public List<MesRequirement> getRequirementList(MesRequirement mesRequirement) {
        return mesRequirementMapper.selectMesRequirementList(mesRequirement);
    }

    /**
     * 新增需求
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMesRequirement(MesRequirement mesRequirement) {
        FabAssertUtils.isNotBlank(mesRequirement.getRequirementName(), BaseErrorCodeEnum.NULL_ERROR, "需求名称为空！");
        MesRequirement require = new MesRequirement();
        require.setRequirementName(mesRequirement.getRequirementName());
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser!= null) {
            require.setDeptId(sysUser.getDeptId());
        }
        int count = count(require);
        if (count > 0) {
            FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "需求名称已存在,请更改需求名称再提交!");
        }
        if (StringUtils.isNotEmpty(mesRequirement.getCreateUid())) {
            SysUser user = sysUserService.selectUserByUserName(mesRequirement.getCreateUid());
            if (user != null) {
                mesRequirement.setCreateUname(user.getNickName());
            }
        }
        if (CollectionUtils.isNotEmpty(mesRequirement.getAssignPersons())) {
            mesRequirement.setAssignPerson(StringUtils.join(mesRequirement.getAssignPersons(), ","));
        }
        MesRequirement requirement = mesRequirementMapper.selectOneByRequirementCodeDesc(mesRequirement);
        String requirementCode;
        String prefix = REQUIREMENT_CODE_PREFIX;
        if (requirement != null) {
            String yearCode = DateUtils.dateTimeNow("yyyy").substring(2);
            if (requirement.getRequirementCode().contains(prefix + yearCode)) {
                int num = Integer.parseInt(requirement.getRequirementCode().substring(requirement.getRequirementCode().length() - 5));
                requirementCode = RequirementUtils.getRequirementCode(prefix, num);
            } else {
                requirementCode = RequirementUtils.getRequirementCode(prefix, 0);
            }
        } else {
            requirementCode = RequirementUtils.getRequirementCode(prefix, 0);
        }
        mesRequirement.setRequirementCode(requirementCode);
        // 设定需求的交付状态
        mesRequirement.setDeliveryStatus(mesRequirementService.getDeliveryStatus(mesRequirement));
        int num = mesRequirementMapper.insert(mesRequirement);
        if (num > 0) {
            mesOaRequirementService.updateOaRequirementStatus(mesRequirement.getRequirementOaId());
            // 需求变动引起项目变动
            mesRequirementService.changeProjectStatus(mesRequirement);
        }
        return num;
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mesRequirement 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMesRequirement(MesRequirement mesRequirement)
    {
        if (CollectionUtils.isNotEmpty(mesRequirement.getAssignPersons())) {
            mesRequirement.setAssignPerson(StringUtils.join(mesRequirement.getAssignPersons(), ","));
        }
        return mesRequirementMapper.updateMesRequirement(mesRequirement);
    }

    @Override
    public boolean updateBatchMesRequirementById(List<MesRequirement> mesRequirements) {
        return this.updateBatchById(mesRequirements);
    }

    /**
     * 批量删除当前需求和当前需求下的所有任务
     *
     * @param requirementCodes 需要删除的需求code
     * @return 结果
     */
    @Override
    @Transactional
    public boolean deleteMesRequirementByRequirementCodes(List<String> requirementCodes)
    {
        // 查询删除需求的信息（主要是project_code)
        MesRequirement mesRequirement = mesRequirementService.getById(requirementCodes.get(0));
        mesRequirementService.removeBatchByIds(requirementCodes);
        mesRequirementTaskService.deleteMesRequirementTaskByRequirementCode(requirementCodes);
        // 需求变化引起项目变化
        mesRequirementService.changeProjectStatus(mesRequirement);
        return true;
    }

    /**
     * 取消当前需求和当前需求下的所有任务
     *
     * @param mesRequirement 需要取消的需求
     * @return 结果
     */
    @Override
    @Transactional
    public boolean cancelMesRequirementByRequirementCode(MesRequirement mesRequirement)
    {
        mesRequirement.setStatus("Canceled");
        mesRequirementService.updateMesRequirement(mesRequirement);
        mesRequirementTaskService.updateBatchMesRequirementTaskStatus(Collections.singletonList(mesRequirement.getRequirementCode()),"Canceled");
        return true;
    }

    /**
     * 修改当前需求和当前需求下的所有任务
     *
     * @param mesRequirement 需要修改的需求
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateRequirementAndTask(MesRequirement mesRequirement)
    {
        FabAssertUtils.isNotBlank(mesRequirement.getRequirementName(), BaseErrorCodeEnum.NULL_ERROR, "需求名称为空");
        List<MesRequirementTask> mesRequirementTasks = mesRequirementTaskService.selectMesRequirementTaskList(
                MesRequirementTask.builder().requirementCode(mesRequirement.getRequirementCode()).deptId(SecurityUtils.getLoginUser().getUser().getDept().getDeptId()).build());
        // 需求状态改为已完成，但是任务存在进行中或未开始状态，则不允许修改
        if (mesRequirement.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())) {
            // 判断当前需求下的所有任务状态是不是都已完成
            if (mesRequirementTasks.stream().anyMatch(obj -> obj.getStatus()!= null && (obj.getStatus().contains("InProgress") || obj.getStatus().contains("UnStart")))) {
                FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR,"当前需求下存在未完成或未开始的任务，请完成所有任务再修改需求状态为已完成！");
            }
        } else {
            if (mesRequirement.getStatus().equals(RequirementStatusEnum.UN_START.getCode())) {
                if (mesRequirementTasks.stream().anyMatch(obj -> obj.getStatus()!= null && (obj.getStatus().contains("InProgress") || obj.getStatus().contains("Completed")))) {
                    FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR,"当前需求下存在进行中或已完成的任务，不允许修改状态为未开始!");
                }
                mesRequirement.setActualBeginTime(null);
            }
            mesRequirement.setActualEndTime(null);
        }
        // 设定交付状态
        mesRequirement.setDeliveryStatus(mesRequirementService.getDeliveryStatus(mesRequirement));
        // 判断是否能变更需求名称
        int count = count(MesRequirement.builder().requirementCode(mesRequirement.getRequirementCode()).requirementName(mesRequirement.getRequirementName()).build());
        if (count > 0) {
            FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "需求名称已存在,请更改需求名称再提交!");
        }
        SysUser sysUser = sysUserService.selectUserByUserName(mesRequirement.getCreateUid());
        if (sysUser == null) {
            FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "未查询到责任人信息！");
        }
        mesRequirement.setCreateUname(sysUser.getNickName());
        SysUser loginUser = SecurityUtils.getLoginUser().getUser();
        if (loginUser != null) {
            mesRequirement.setUpdateUid(loginUser.getUserName());
            mesRequirement.setUpdateUname(loginUser.getNickName());
        }
        mesRequirement.setUpdateTime(new Date());
        mesRequirementService.updateMesRequirement(mesRequirement);
        mesRequirementService.changeProjectStatus(mesRequirement);
        return true;
    }

    @Override
    public List<MesRequirement> getRequirementCodes(MesRequirement mesRequirement) {
        List<Long> list = new ArrayList<>();
        list.add(mesRequirement.getDeptId());
        list.add(110L);
        SysDept sysDept = SecurityUtils.getLoginUser().getUser().getDept();
        if (sysDept.getDeptId() == 110) {
            list.add(114L);
            list.add(115L);
            list.add(116L);
        }
        mesRequirement.setDeptList(list);
        return mesRequirementMapper.getRequirementCodes(mesRequirement);
    }

    @Override
    public List<MesRequirementVo> generateReleaseDocument(List<String> id) {
        FabAssertUtils.isNotEmpty(id, BaseErrorCodeEnum.NULL_ERROR, "需求ID不能为空");
        List<MesRequirement> mesRequirements = mesRequirementMapper.selectBatchIds(id);
        Date date = new Date();
        long year = date.getYear() + 1900;
        long month = date.getMonth() + 1;
        int count = releaseLogService.getCount(year, month);
        String releaseVersion = "v" + (year - 2023) + "." + month + "." + (count + 1) + "_" + DateUtils.parseDateToStr("yyyyMMddHHmmss", date);
        if (CollectionUtils.isNotEmpty(mesRequirements)) {
            for (MesRequirement mesRequirement : mesRequirements) {
                if (StringUtils.isBlank(mesRequirement.getReleaseVersion())) {
                    mesRequirement.setReleaseVersion(releaseVersion);
                    continue;
                }
                if (!mesRequirement.getReleaseVersion().contains(releaseVersion)) {
                    mesRequirement.setReleaseVersion(mesRequirement.getReleaseVersion() + "," + releaseVersion);
                }
            }
        }
        mesRequirementService.updateBatchById(mesRequirements);
        List<MesRequirementVo> mesRequirementVos = mesRequirementService.getReleaseDocument(id);
        String releaseDescInfo = null;
        if (CollectionUtils.isNotEmpty(mesRequirementVos)) {
            for (MesRequirementVo mesRequirementVo : mesRequirementVos) {
                mesRequirementVo.setReleaseVersion(releaseVersion);
                if (releaseDescInfo == null) {
                    releaseDescInfo = mesRequirementVo.getReleaseContent();
                } else {
                    releaseDescInfo = releaseDescInfo + "<br>" + mesRequirementVo.getReleaseContent();
                }
            }
        }
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        MesReleaseLog mesReleaseLog = MesReleaseLog.builder()
                .timeKey(Long.valueOf(DateUtils.dateTimeNow()))
                .year((int)year)
                .month((int)month)
                .releaseVersion(releaseVersion)
                .applyDate(date)
                .applyUname(sysUser.getNickName())
                .applyUid(sysUser.getUserName())
                .status("InProgress")
                .releaseDescInfo(releaseDescInfo)
                .build();
        releaseLogService.insertMesReleaseLog(mesReleaseLog);
        return mesRequirementVos;
    }

    @Override
    public List<MesRequirementVo> getReleaseDocument(List<String> ids) {
        List<MesRequirementVo> mesRequirementVos =  mesRequirementMapper.generateReleaseDocument(ids);
        List<SysUser> sysUsers = sysUserService.selectUserList(new SysUser());
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mesRequirementVos)) {
            map = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserName, SysUser::getNickName));
        }
        if (CollectionUtils.isNotEmpty(mesRequirementVos)) {
            Map<String, String> finalMap = map;
            mesRequirementVos.forEach(value -> {
                String content = (StringUtils.isNotBlank(value.getRequirementName()) ? value.getRequirementName() : "unKnow") +
                        " 【需求部门：" +
                        (StringUtils.isNotBlank(value.getRequirementDept()) ? value.getRequirementDept() : "unKnow") +
                        "】【需求用户：" +
                        (StringUtils.isNotBlank(value.getRequirementUser()) ? value.getRequirementUser() : "unKnow") +
                        "】";
                value.setReleaseContent(content);
                String developers;
                if (StringUtils.isNotBlank(value.getDevelopers())) {
                    developers = finalMap.get(value.getDevelopers()) != null ? finalMap.get(value.getDevelopers()) : "unKnow";
                } else {
                    developers = finalMap.get(value.getCreateUid()) != null ? finalMap.get(value.getCreateUid()) : "unKnow";
                }
                value.setDevelopers(developers);
            });
        }
        return mesRequirementVos;
    }

    @Override
    public List<MesRequirement> getRequirementsByUserName() {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        String userName = sysUser.getUserName();
        MesRequirement mesRequirement = new MesRequirement();
        mesRequirement.setAssignPerson(userName);
        mesRequirement.setEndTime(DateUtils.addDays(DateUtils.parseDate(DateUtils.getDate()), -3));
        return mesRequirementMapper.getRequirementsByUserName(mesRequirement);
    }

    @Override
    public String getDeliveryStatus(MesRequirement mesRequirement) {
        String deliveryStatus = null;
        if (mesRequirement.getStatus().equals(RequirementStatusEnum.UN_START.getCode()) && mesRequirement.getBeginTime().before(DateUtils.parseDate(DateUtils.getDate()))) {
            deliveryStatus = DeliveryStatusEnum.NON_START.getCode();
        } else if (mesRequirement.getStatus().equals(RequirementStatusEnum.IN_PROGRESS.getCode())) {
            deliveryStatus = mesRequirement.getEndTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.ON_GOING.getCode();
        } else if (mesRequirement.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())){
            deliveryStatus = mesRequirement.getEndTime().before(mesRequirement.getActualEndTime()) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.COMPLETED.getCode();
        }
        return deliveryStatus;
    }

    private int count(MesRequirement mesRequirement) {
        return mesRequirementMapper.countByName(mesRequirement);
    }

    /**
     * @param mesRequirement
     * 查找项目下需求的最小计划开始时间和最大计划结束时间
     */
    @Override
    public MesRequirement selectTimeEndpointForProject(MesRequirement mesRequirement) {
        return mesRequirementMapper.selectTimeEndpointForProject(mesRequirement);
    }

    /**
     * 需求改变引起项目变动
     * @param mesRequirement
     */
    @Override
    public void changeProjectStatus(MesRequirement mesRequirement) {
        if (mesRequirement == null ){
            return;
        }
        MesRequirement timeEndpoint = mesRequirementService.selectTimeEndpointForProject(mesRequirement);
        if (StringUtils.isNotEmpty(mesRequirement.getProjectCode())) {
            List<String> tempList = new ArrayList<>();
            MesProject project = mesProjectService.selectMesProjectByProjectCode(mesRequirement.getProjectCode());
            if (project != null) {
                // 需求不存在最早实际开始和最晚实际结束时间 状态设为未开始，
                // 需求存在最早实际开始和最晚实际结束时间 需求状态不是已完成则设为进行中，是已完成且当前任务状态也为已完成则需求状态是已完成
                // 需求实际最早开始时间，不存在最晚实际结束时间，需求状态设为进行中
                String status = RequirementStatusEnum.COMPLETED.getCode();
                String deliveryStatus = null;
                Date actualEndTime = null;
                if (timeEndpoint == null || timeEndpoint.getMinActualBeginTime() == null && timeEndpoint.getMaxActualEndTime() == null) {
                    status = RequirementStatusEnum.UN_START.getCode();
                    deliveryStatus = project.getBeginTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.NON_START.getCode() : null;
                } else if (timeEndpoint.getMinActualBeginTime() != null && timeEndpoint.getMaxActualEndTime() == null || !Objects.equals(project.getStatus(), RequirementStatusEnum.COMPLETED.getCode())) {
                    status = RequirementStatusEnum.IN_PROGRESS.getCode();
                    deliveryStatus = project.getEndTime().before(DateUtils.parseDate(DateUtils.getDate())) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.ON_GOING.getCode();
                } else if (timeEndpoint.getMinActualBeginTime() != null && Objects.equals(project.getStatus(), RequirementStatusEnum.COMPLETED.getCode()) && project.getStatus().equals(RequirementStatusEnum.COMPLETED.getCode())) {
                    deliveryStatus = project.getEndTime().before(timeEndpoint.getMaxActualEndTime()) ? DeliveryStatusEnum.OVER_DUE.getCode() : DeliveryStatusEnum.COMPLETED.getCode();
                    actualEndTime = timeEndpoint.getMaxActualEndTime();
                }
                project.setStatus(status);
                project.setDeliveryStatus(deliveryStatus);
                project.setActualBeginTime(timeEndpoint == null ? null :timeEndpoint.getMinActualBeginTime());
                project.setActualEndTime(actualEndTime);
                mesProjectService.updateById(project);
            }
        }
    }

    /**
     * 导出
     * @param mesRequirement
     * @param response
     */
    @Override
    public void export(MesRequirement mesRequirement, HttpServletResponse response) {
        response.reset();
        String fileName = "requirement_" + LocalDate.now() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "");
        response.setContentType("application/octet-stream; charset=UTF-8");
        try {
            List<ExportRequirementVo> exportRequirementVos = new ArrayList<>();
            List<MesRequirement> mesRequirements = mesRequirementMapper.selectMesRequirementList(mesRequirement);
            if (CollectionUtils.isNotEmpty(mesRequirements)) {
                mesRequirements.forEach(item -> {
                    ExportRequirementVo vo = new ExportRequirementVo();
                    BeanUtils.copyProperties(item, vo);
                    // code和value转换
                    Map<String, String> reviewStatusMap = ImmutableMap.of("Accepted","已接收", "Approved","评审通过", "Failed","评审未通过");
                    Map<String, String> statusMap = ImmutableMap.of("UnStart","未开始", "InProgress","进行中", "Completed","已完成");
                    Map<Long, String> deptMap = ImmutableMap.of(110L,"制造信息部", 116L,"MES组", 115L,"数据应用组",114L,"设备应用组");
                    Map<String, String> attributeMap = ImmutableMap.of("Optimization","优化", "Project","项目");
                    vo.setReviewStatus(reviewStatusMap.get(vo.getReviewStatus()));
                    vo.setStatus(statusMap.get(vo.getStatus()));
                    vo.setDeptName(deptMap.get(vo.getDeptId()));
                    vo.setAttribute(attributeMap.get(vo.getAttribute()));
                    exportRequirementVos.add(vo);
                });
            }
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet sheet = EasyExcel.writerSheet().head(ExportRequirementVo.class).build();
            excelWriter.write(exportRequirementVos, sheet);
            excelWriter.finish();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("导出异常。", e);
        }
    }
}
