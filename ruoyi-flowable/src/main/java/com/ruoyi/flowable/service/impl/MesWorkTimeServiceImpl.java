package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.config.WeLinkAsyncProperties;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.flowable.domain.dto.*;
import com.ruoyi.flowable.mapper.MesWorkTimeMapper;
import com.ruoyi.flowable.service.IMesDimTdDateService;
import com.ruoyi.flowable.service.IMesWorkTimeService;
import com.ruoyi.flowable.util.CurrentWeekUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2024-04-10
 */
@Service
public class MesWorkTimeServiceImpl extends ServiceImpl<MesWorkTimeMapper, MesWorkTime> implements IMesWorkTimeService {

    @Autowired
    private MesWorkTimeMapper mesWorkTimeMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IMesDimTdDateService mesDimTdDateService;

    @Resource
    private WeLinkAsyncProperties weLinkAsyncProperties;
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private IMesWorkTimeService mesWorkTimeService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MesWorkTime selectMesWorkTimeById(Long id)
    {
        return this.getById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesWorkTime> selectMesWorkTimeList(MesWorkTime mesWorkTime) {
        SysUser sysUser = SecurityUtils.getLoginUser().getUser();
        if (sysUser != null) {
            List<SysRole> sysRoles = sysUser.getRoles();
            if (CollectionUtils.isNotEmpty(sysRoles)) {
                boolean flag = false;
                for (SysRole sysRole : sysRoles) {
                    if (sysRole.getRoleKey().equals("flow_admin")) {
                        flag = true;
                       break;
                    }
                }
                if (!flag) {
                    mesWorkTime.setWorkUser(sysUser.getUserName());
                }
            }
        }
        List<MesWorkTime> mesWorkTimes = mesWorkTimeMapper.selectMesWorkTimeList(mesWorkTime);
        /** 每天的工时汇总
        * 1. 先按日期分组
        * 2. 计算每个日期的小时数
        */
        if (CollectionUtils.isNotEmpty(mesWorkTimes)) {
            Map<String, List<String>> map = mesWorkTimes.stream().collect(Collectors.groupingBy(obj -> obj.getWorkDate() + "&" + obj.getWorkUser(), Collectors.mapping(MesWorkTime::getWorkHour, Collectors.toList())));
            Map<String, Double> hourMap = new HashMap<>();
            map.forEach((key, value) -> {
                try {
                    Double totalHour = value.stream().mapToDouble(Double::parseDouble).sum();
                    hourMap.put(key, totalHour);
                } catch (Exception e) {
                    log.error(key + "工时汇总异常", e);
                }
            });
            mesWorkTimes.forEach(value -> {
                value.setTotalHour(hourMap.get(value.getWorkDate() + "&" + value.getWorkUser()));
            });
        }
        return mesWorkTimes;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMesWorkTime(MesWorkTime mesWorkTime)
    {
        String week = CurrentWeekUtils.getCurrentWeek(mesWorkTime.getWorkDate());
        mesWorkTime.setWeekOfYear(week);
        if (!mesWorkTime.getPeriodType().equals("加班")) {
            mesWorkTimeService.checkWorkHour(mesWorkTime);
        }
        return mesWorkTimeMapper.insert(mesWorkTime);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMesWorkTime(MesWorkTime mesWorkTime)
    {
        String week = CurrentWeekUtils.getCurrentWeek(mesWorkTime.getWorkDate());
        mesWorkTime.setWeekOfYear(week);
        if (!mesWorkTime.getPeriodType().equals("加班")) {
            mesWorkTimeService.checkWorkHour(mesWorkTime);
        }
        return mesWorkTimeMapper.updateById(mesWorkTime);
    }

    @Override
    public void checkWorkHour(MesWorkTime mesWorkTime) {
        String formatDate = com.alibaba.fastjson2.util.DateUtils.format(mesWorkTime.getWorkDate(),"yyyy-MM-dd");
        List<MesWorkTime> mesWorkTimeList = mesWorkTimeService.lambdaQuery().eq(MesWorkTime::getWorkDate,mesWorkTime.getWorkDate())
                .eq(MesWorkTime::getWorkUser,mesWorkTime.getWorkUser())
                .in(MesWorkTime::getPeriodType,Arrays.asList("正常","请假"))
                .list();
        if (mesWorkTime.getId() != null) {
            mesWorkTimeList = mesWorkTimeList.stream().filter(item -> ! item.getId().equals(mesWorkTime.getId())).collect(Collectors.toList());
        }
        double sumHour = mesWorkTimeList.stream()
                .mapToDouble(item -> Double.parseDouble(item.getWorkHour()))
                .sum();
        FabAssertUtils.isTrue((sumHour + Double.parseDouble(mesWorkTime.getWorkHour())) <= 8, BaseErrorCodeEnum.PARAM_ERROR,formatDate + "日请假和正常工作时长总和超过 8 小时！");
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMesWorkTimeByIds(List<String> ids)
    {
        return mesWorkTimeMapper.deleteBatchIds(ids);
    }

    /**
     * 获取填报人
     *
     * @return 结果
     */
    @Override
    public List<SysUser> getWorkUser() {
        return sysUserService.getUsersByDeptId(110L);
    }

    @Override
    public void sendMessageTaskToIndividual() {
        try {
            String formatDate = mesWorkTimeService.getQueryDate();
            if (StringUtils.isEmpty(formatDate)) {
                return;
            }
            WeLinkCommonCardRequest request = new WeLinkCommonCardRequest();
            request.setMsgOwner("msgOwner");
            request.setMsgTitle("工时管理超时未填写通知");
            request.setMsgContent("您" + formatDate + "的工时管理超时未完成填写，请点击链接及时填写。\n请确保正常工作和请假总时长不小于8小时。\n注意：超过中午12点未补充完成将推送至制造信息群并@相应部门经理");
            request.setUrlPath(weLinkAsyncProperties.getWeLinkCommonCardUrlPath());
            request.setPublicAccID(weLinkAsyncProperties.getWeLinkCommonCardPublicAccID());
            MesWorkTime mesWorkTime = new MesWorkTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mesWorkTime.setWorkDate(dateFormat.parse(formatDate));
            List<SysUser> users = mesWorkTimeService.getUncompleteTimeCardList(mesWorkTime,"daily");
            if (CollectionUtils.isEmpty(users)) {
                return;
            }
            request.setToUserList(users.stream().map(SysUser::getUserName).collect(Collectors.toList()));
            restTemplate.postForObject(weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendCommonCardUrl(), request, Object.class);
        } catch (Exception e) {
            log.error("sendMessageTaskToIndividualTask error:", e);
        }
    }

    @Override
    public void sendMessageTaskToManage() {
        try {
            List<String> workDateList = mesWorkTimeService.getQueryDateList();
            if (CollectionUtils.isEmpty(workDateList)) {
                return;
            }
            String formatBeginDate = workDateList.get(0);
            String formatEndDate = workDateList.get(workDateList.size() - 1);
            MesWorkTime mesWorkTime = new MesWorkTime();
            mesWorkTime.setWeeklyWorkDateList(workDateList);
            mesWorkTime.setWeeklyWorkHour((double) (workDateList.size() * 8));
            List<SysUser> users = mesWorkTimeService.getUncompleteTimeCardList(mesWorkTime,"weekly");
            if (CollectionUtils.isEmpty(users)) {
                return;
            }
            String content = users.stream()
                    .collect(Collectors.groupingBy(user -> user.getDept().getDeptName(),
                            Collectors.mapping(SysUser::getNickName, Collectors.joining(","))))
                    .entrySet().stream()
                    .map(entry -> entry.getKey() + ":" + entry.getValue())
                    .collect(Collectors.joining("\n"));
            WeLinkCommonCardRequest request = new WeLinkCommonCardRequest();
            request.setMsgOwner("msgOwner");
            request.setMsgTitle("工时管理超时未填写名单");
            request.setPublicAccID(weLinkAsyncProperties.getWeLinkCommonCardPublicAccID());
            request.setMsgContent(formatBeginDate + "~" + formatEndDate + "工时管理超时未填写满" + workDateList.size()  + "天名单：\n" + content);
            request.setToUserList(Arrays.asList("1109771","1039303","1042314","1038151","1035781"));
            restTemplate.postForObject(weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendCommonCardUrl(), request, Object.class);
        } catch (Exception e) {
            log.error("sendMessageTaskToManageTask error:", e);
        }
    }

    @Override
    public void sendRobotMsg() {
        try {
            String formatDate = mesWorkTimeService.getQueryDate();
            if (StringUtils.isEmpty(formatDate)) {
                return;
            }
            StringBuilder content = new StringBuilder(formatDate + "工时管理超时未填写名单推送 ");
            MesWorkTime mesWorkTime = new MesWorkTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mesWorkTime.setWorkDate(dateFormat.parse(formatDate));
            List<SysUser> resultList = mesWorkTimeService.getUncompleteTimeCardList(mesWorkTime,"daily");
            if (CollectionUtils.isEmpty(resultList)) {
                return;
            }
            Map<String, List<String>> resultMap = resultList.stream()
                    .collect(Collectors.groupingBy(user -> user.getDept().getDeptName(),
                            Collectors.mapping(SysUser::getNickName, Collectors.toList())));
            List<NameValueDto> nameValueDtoList = new ArrayList<>();
            List<String> atList = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
                NameValueDto nameValueDto = new NameValueDto();
                nameValueDto.setName(entry.getKey());
                nameValueDto.setValue(entry.getValue().toString());
                nameValueDto.setSeq(i++);
                nameValueDtoList.add(nameValueDto);
                String atPerson = entry.getKey().equals("MES组") ? "1039303" : entry.getKey().equals("设备应用组") ? "1042314" : entry.getKey().equals("数据应用组") ? "1038151" : "";
                String atPersonName = entry.getKey().equals("MES组") ? "蔡文文" : entry.getKey().equals("设备应用组") ? "龚俊郎" : entry.getKey().equals("数据应用组") ? "陶学相" : "";
                if (!atPerson.isEmpty()) {
                    atList.add(atPerson);
                }
                if (!atPersonName.isEmpty()) {
                    content.append("@").append(atPersonName).append(" ");
                }
            }
            nameValueDtoList.add(new NameValueDto("请各领导督促本部门成员完成工时管理填写","每周第一个工作日下午三点推送前一周未完成工时管理名单至经理",i));
            WeLinkSendRequest request = new WeLinkSendRequest();
            request.setRobotUrl(weLinkAsyncProperties.getWeLinkRobotUrl());
            request.setIsAtFlag(true);
            request.setContent(String.valueOf(content));
            request.setMessageBody(nameValueDtoList);
            request.setAtUserList(atList);
            restTemplate.postForObject(weLinkAsyncProperties.getAddress() + weLinkAsyncProperties.getSendRobotMsgUrl(), request, Object.class);
        } catch (Exception e) {
            log.error("sendRobotMsgTask error:", e);
        }
    }

    /**
     * 获取推送timecard未填写的日期:今天的前一个工作日
     * @return
     */
    @Override
    public String getQueryDate() {
        // 当天不是工作日，不推送
        String today = DateUtils.getDate();
        MesDimTdDate todayDate = mesDimTdDateService.lambdaQuery().eq(MesDimTdDate::getFullDate, today)
                .eq(MesDimTdDate::getWeekdayFlag,'Y')
                .eq(MesDimTdDate::getVacationDayFlag, 'N')
                .one();
        if(todayDate == null){
            return null;
        }
        String workDate = null;
        MesDimTdDate mesDimTdDate = mesDimTdDateService.lambdaQuery().lt(MesDimTdDate::getFullDate, today)
                .eq(MesDimTdDate::getWeekdayFlag,'Y')
                .eq(MesDimTdDate::getVacationDayFlag, 'N')
                .orderByDesc(MesDimTdDate::getFullDate).last("limit 1").one();
        if(!Objects.isNull(mesDimTdDate)){
            workDate = mesDimTdDate.getFullDate();
        }
        return workDate;
    }

    /**
     * 获取推送timecard未填写的日期:上周的工作日
     * @return
     */
    @Override
    public List<String> getQueryDateList() {
        // 判断是不是当前周的第一个工作日 是则推送上一周工作日未完成名单，不是则不推送
        LocalDateTime date = LocalDateTime.now();
        // 当前周
        Integer week = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        // 获取当前周第一个工作日
        MesDimTdDate mesDimTdDate = mesDimTdDateService.lambdaQuery()
                .eq(MesDimTdDate::getWeekNumInYear,week)
                .eq(MesDimTdDate::getWeekdayFlag,'Y')
                .eq(MesDimTdDate::getVacationDayFlag, 'N')
                .orderByAsc(MesDimTdDate::getFullDate).last("limit 1").one();
        if (!mesDimTdDate.getFullDate().equals(date.format(DateTimeFormatter.ISO_DATE))) {
            return null;
        }
        // 获取上一周工作日列表
        List<MesDimTdDate> workDayList = mesDimTdDateService.lambdaQuery()
                .eq(MesDimTdDate::getWeekdayFlag,'Y')
                .eq(MesDimTdDate::getVacationDayFlag, 'N')
                .eq(MesDimTdDate::getWeekNumInYear,week - 1)
                .orderByAsc(MesDimTdDate::getFullDate)
                .list();
        if (CollectionUtils.isEmpty(workDayList)) {
            return null;
        }
        List<String> MesDimTdDate = workDayList.stream().map(com.ruoyi.flowable.domain.dto.MesDimTdDate::getFullDate).collect(Collectors.toList());
        return MesDimTdDate;
    }

    @Override
    public List<SysUser> getUncompleteTimeCardList(MesWorkTime mesWorkTime,String type) {
        //已完成名单
        List<MesWorkTime> completeTimeCardList = new ArrayList<>();
        if (type.equals("daily")) {
            completeTimeCardList = mesWorkTimeService.selectCompleteTimeCardList(mesWorkTime);
        } else if (type.equals("weekly")) {
            completeTimeCardList = mesWorkTimeService.selectWeeklyCompleteTimeCardList(mesWorkTime);
        }
        List<String> userList = completeTimeCardList.stream().map(MesWorkTime::getWorkUser).collect(Collectors.toList());
        List<Long> deptList = Arrays.asList(114L, 115L, 116L);
        // 所有用户列表
        List<SysUser> fullList = sysUserService.selectUserListForTask(new SysUser());
        List<SysUser> resultList = fullList.stream().filter(user -> !userList.contains(user.getUserName()) && deptList.contains(user.getDeptId())).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public List<MesWorkTime> selectCompleteTimeCardList(MesWorkTime mesWorkTime){
        return baseMapper.selectDailyCompleteTimeCardList(mesWorkTime);
    }

    @Override
    public List<MesWorkTime> selectWeeklyCompleteTimeCardList(MesWorkTime mesWorkTime){
        return baseMapper.selectWeeklyCompleteTimeCardList(mesWorkTime);
    }

    @Override
    public List<MesWorkTime> selectTimeCardListForOwn(MesWorkTime mesWorkTime) {
        return baseMapper.selectTimeCardListForOwn(mesWorkTime);
    }

    @Override
    public List<MesWorkTime> selectTotalTimeCardListForOwn(MesWorkTime mesWorkTime) {
        return baseMapper.selectTotalTimeCardListForOwn(mesWorkTime);
    }

    @Override
    public Map<String,List<MesWorkTime>> selectTimeCardList(MesWorkTime mesWorkTime) {
        if (mesWorkTime.getWorkUser() == null) {
            mesWorkTime.setWorkUser(SecurityUtils.getLoginUser().getUsername());
        }
        // 工时明细
        List<MesWorkTime> mesWorkTimeList = mesWorkTimeService.selectTimeCardListForOwn(mesWorkTime);
        // 工时汇总
        List<MesWorkTime> mesWorkTimeTotal = mesWorkTimeService.selectTotalTimeCardListForOwn(mesWorkTime);
        // 合并明细和汇总数据
        for (MesWorkTime mesWorkTime1 : mesWorkTimeTotal) {
            mesWorkTime1.setTaskName("工时汇总");
        }
        Map<String,List<MesWorkTime>> timeCardMap = new HashMap<>();
        timeCardMap.put("data",mesWorkTimeList);
        timeCardMap.put("total",mesWorkTimeTotal);
        return timeCardMap;
    }

    @Override
    public MesWorkTime getProportionOfWorkTypes(MesWorkTime mesWorkTime) {
        List<String> groupFieldList = new ArrayList<>();
        if (StringUtils.isNotEmpty(mesWorkTime.getWorkUser())) {
            groupFieldList.add("work_user");
        }
        if (StringUtils.isNotEmpty(mesWorkTime.getWeekOfYear())) {
            groupFieldList.add("week_of_year");
        }
        mesWorkTime.setGroupFieldList(groupFieldList);
        List<MesWorkTime> workTypeDurations = mesWorkTimeMapper.selectWorkTypeDuration(mesWorkTime);
        MesWorkTime totalHourVo = mesWorkTimeMapper.selectWeeklyTotalTime(mesWorkTime);
        if (totalHourVo != null) {
            Double weeklyTotalHour = totalHourVo.getTotalHour();
            mesWorkTime.setTotalHour(weeklyTotalHour);
            mesWorkTime.setOptimizationProportion(mesWorkTimeService.countProportion("Optimization",weeklyTotalHour,workTypeDurations));
            mesWorkTime.setProjectProportion(mesWorkTimeService.countProportion("Project", weeklyTotalHour, workTypeDurations));
            mesWorkTime.setOperationProportion(mesWorkTimeService.countProportion("Operation",weeklyTotalHour,workTypeDurations));
            mesWorkTime.setManagementProportion(mesWorkTimeService.countProportion("Management",weeklyTotalHour,workTypeDurations));
            mesWorkTime.setStudyProportion(mesWorkTimeService.countProportion("Study",weeklyTotalHour,workTypeDurations));
            return mesWorkTime;
        }
        return null;
    }

    @Override
    public String countProportion(String workType, Double weeklyTotalHour,List<MesWorkTime> workTypeDurations) {
        DecimalFormat decFormat = new DecimalFormat("###.##%");
        String proportion = "0";
        if (workTypeDurations.stream().anyMatch(item -> item.getWorkType().equals(workType)) && weeklyTotalHour > 0) {
            proportion = decFormat.format(workTypeDurations.stream().filter(item -> item.getWorkType().equals(workType)).findFirst().get().getTotalHour() / weeklyTotalHour);
        }
        return proportion;
    }

    @Override
    public MesWorkTime selectMaxWorkTime(MesWorkTime mesWorkTime) {
        return mesWorkTimeMapper.selectMaxWorkTime(mesWorkTime);
    }

    @Override
    public MesWorkTime getLastTimeCardInfo(MesWorkTime mesWorkTime) {
        return mesWorkTimeService.lambdaQuery()
                .eq(MesWorkTime::getWorkUser, mesWorkTime.getWorkUser())
                .eq(MesWorkTime::getWorkType, mesWorkTime.getWorkType())
                .eq(StringUtils.isNotEmpty(mesWorkTime.getWorkCode()), MesWorkTime::getWorkCode, mesWorkTime.getWorkCode())
                .orderByDesc(MesWorkTime::getCreateTime)
                .last("limit 1").one();
    }

    @Override
    public MesWorkTime getLastWorkDayMsg(MesWorkTime mesWorkTime){
        double LastWeekDayTotalHour = 8.0;
        MesDimTdDate lastWorkDate = mesDimTdDateService.lambdaQuery().lt(MesDimTdDate::getFullDate, new SimpleDateFormat("yyyy-MM-dd").format(mesWorkTime.getWorkDate()))
                .eq(MesDimTdDate::getWeekdayFlag,'Y')
                .eq(MesDimTdDate::getVacationDayFlag,'N')
                .orderByDesc(MesDimTdDate::getFullDate).last("limit 1").one();
        if (lastWorkDate == null) {
            mesWorkTime.setTotalHour(LastWeekDayTotalHour);
            return mesWorkTime;
        }
        String lastWorkDayStr = lastWorkDate.getFullDate();
        LastWeekDayTotalHour = 0.0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(lastWorkDayStr, formatter);
        Date lastWorkDay = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<MesWorkTime> lastWorkDayList = mesWorkTimeService.lambdaQuery()
                .eq(MesWorkTime::getWorkDate, lastWorkDay)
                .eq(MesWorkTime::getWorkUser,mesWorkTime.getWorkUser())
                .in(MesWorkTime::getPeriodType, Arrays.asList("正常", "请假")).list();
        if (CollectionUtils.isNotEmpty(lastWorkDayList)) {
           LastWeekDayTotalHour = lastWorkDayList.stream()
                    .mapToDouble(item -> Double.parseDouble(item.getWorkHour()))
                    .sum();
        }
        mesWorkTime.setWorkDate(lastWorkDay);
        mesWorkTime.setTotalHour(LastWeekDayTotalHour);
        return mesWorkTime;
    }

}
