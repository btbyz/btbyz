package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.flowable.domain.dto.MesWorkTime;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2024-04-10
 */
public interface IMesWorkTimeService extends IService<MesWorkTime> {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MesWorkTime selectMesWorkTimeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MesWorkTime> selectMesWorkTimeList(MesWorkTime mesWorkTime);

    /**
     * 新增【请填写功能名称】
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 结果
     */
    public int insertMesWorkTime(MesWorkTime mesWorkTime);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 结果
     */
    public int updateMesWorkTime(MesWorkTime mesWorkTime);

    /**
     * 校验填入的工时是否合法
     *
     * @param mesWorkTime 【请填写功能名称】
     * @return 结果
     */
    void checkWorkHour(MesWorkTime mesWorkTime);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteMesWorkTimeByIds(List<String> ids);

    /**
     * 获取填报人
     *
     * @return 结果
     */
    public List<SysUser> getWorkUser();

    /**
     * 推送公众号给个人任务
     * @return
     */
    void sendMessageTaskToIndividual();

    /**
     * 通过公众号推送未完成名单给经理
     * @return
     */
    void sendMessageTaskToManage();

    /**
     * 推送群组并@任务
     * @return
     */
    void sendRobotMsg();

    /**
     * 获取推送timecard未填写的日期:今天的前一个工作日
     * @return
     */
    String getQueryDate();

    /**
     * 获取推送timecard未填写的日期:上周的工作日
     * @return
     */
    List<String> getQueryDateList();

    /**
     * 获取每天/每周工时管理未完成人员的人员名单
     * @param mesWorkTime
     * @param type
     * @return
     */
    List<SysUser> getUncompleteTimeCardList(MesWorkTime mesWorkTime,String type);

    /**
     * 获取每天工时管理已完成人员的人员名单
     * @param mesWorkTime
     * @return
     */
    List<MesWorkTime> selectCompleteTimeCardList(MesWorkTime mesWorkTime);

    /**
     * 获取每周工时管理已完成人员的人员名单
     * @param mesWorkTime
     * @return
     */
    List<MesWorkTime> selectWeeklyCompleteTimeCardList(MesWorkTime mesWorkTime);

    /**
     * 按任务统计用户每周的timeCard记录
     * @param mesWorkTime
     * @return
     */
    List<MesWorkTime> selectTimeCardListForOwn(MesWorkTime mesWorkTime);

    /**
     * 按天汇总用户一周的timeCard
     * @param mesWorkTime
     * @return
     */
    List<MesWorkTime> selectTotalTimeCardListForOwn(MesWorkTime mesWorkTime);

    /**
     * 获取用户每周的timeCard记录
     * @param mesWorkTime
     * @return
     */
    Map<String,List<MesWorkTime>> selectTimeCardList(MesWorkTime mesWorkTime);

    /**
     * 获取用户每周的几种工作类型占比
     * @param mesWorkTime
     * @return
     */
    MesWorkTime getProportionOfWorkTypes(MesWorkTime mesWorkTime);

    String countProportion(String workType, Double weeklyTotalHour, List<MesWorkTime> workTypeDurations);

    /**
     * 最新任务最新一条工时管理记录的填写日期
     * @param mesWorkTime
     * @return
     */
    MesWorkTime selectMaxWorkTime(MesWorkTime mesWorkTime);

    /**
     * 查询当前任务的上一条工时管理填写记录
     * @param mesWorkTime
     */
    MesWorkTime getLastTimeCardInfo(MesWorkTime mesWorkTime);

    /**
     * 查询当前填写日期的前一个工作日是否满足8小时
     */
    MesWorkTime getLastWorkDayMsg(MesWorkTime mesWorkTime);
}
