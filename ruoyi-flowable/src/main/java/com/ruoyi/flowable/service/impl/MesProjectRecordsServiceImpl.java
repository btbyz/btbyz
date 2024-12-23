package com.ruoyi.flowable.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.flowable.domain.dto.MesProcessReleaseLog;
import com.ruoyi.flowable.domain.dto.MesProjectRecords;
import com.ruoyi.flowable.mapper.MesProcessReleaseLogMapper;
import org.springframework.stereotype.Service;
import com.ruoyi.flowable.mapper.MesProjectRecordsMapper;
import com.ruoyi.flowable.service.IMesProjectRecordsService;

import javax.annotation.Resource;

/**
 * 需求项目记录Service业务层处理
 * 
 * @author wangxin
 * @date 2024-03-11
 */
@Service
public class MesProjectRecordsServiceImpl implements IMesProjectRecordsService {
    @Resource
    private MesProjectRecordsMapper MesProjectRecordsMapper;
    @Resource
    private MesProcessReleaseLogMapper mesProcessReleaseLogMapper;
    /**
     * 查询需求项目记录
     * 
     * @param id 需求项目记录ID
     * @return 需求项目记录
     */
    @Override
    public MesProjectRecords selectMesProjectRecordsById(String id) {
        return MesProjectRecordsMapper.selectMesProjectRecordsById(id);
    }

    /**
     * 查询需求项目记录列表
     * 
     * @param mesProjectRecords 需求项目记录
     * @return 需求项目记录
     */
    @Override
    public List<MesProjectRecords> selectMesProjectRecordsList(MesProjectRecords mesProjectRecords) {
        return MesProjectRecordsMapper.selectMesProjectRecordsList(mesProjectRecords);
    }

    /**
     * 新增需求项目记录
     *
     * @param mesProjectRecords 需求项目记录
     * @return 结果
     */
    @Override
    public boolean insertMesProjectRecords(MesProjectRecords mesProjectRecords) {
        return MesProjectRecordsMapper.insertMesProjectRecords(mesProjectRecords);
    }

    /**
     * 修改需求项目记录
     *
     * @param mesProjectRecords 需求项目记录
     * @return 结果
     */
    @Override
    public boolean updateMesProjectRecords(MesProjectRecords mesProjectRecords) {
        return MesProjectRecordsMapper.updateMesProjectRecords(mesProjectRecords);
    }

    /**
     * 批量删除需求项目记录
     *
     * @param id 需要删除的需求项目记录ID
     * @return 结果
     */
    @Override
    public boolean deleteMesProjectRecordsByIds(String[] id) {
        return MesProjectRecordsMapper.deleteMesProjectRecords(id);
    }

    @Override
    public boolean generateReleaseDocument(Long[] id) {
        if (CollectionUtils.isNotEmpty(Arrays.asList(id))) {
            List<MesProjectRecords> mesProjectRecords = MesProjectRecordsMapper.getMesProjectRecordsList(id);
            List<String> contentList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mesProjectRecords)) {
                mesProjectRecords.forEach(value -> {
                    String content = "[新增]" +
                            (StringUtils.isNotBlank(value.getFactoryArea()) ? value.getFactoryArea() : "unKnow") +
                            ": " +
                            (StringUtils.isNotBlank(value.getProject()) ? value.getProject() : "unKnow") +
                            " (开发人员: " +
                            (StringUtils.isNotBlank(value.getDevelopers()) ? value.getDevelopers() : "unKnow") +
                            "; 用户: " +
                            (StringUtils.isNotBlank(value.getUser()) ? value.getUser() : "unKnow") +
                            ")";
                    contentList.add(content);
                });
            }
            if (CollectionUtils.isNotEmpty(contentList)) {
                String content = String.join("&&", contentList);
                Date date = new Date();
                long year = date.getYear() + 1900;
                long month = date.getMonth() + 1;
                Long count = mesProcessReleaseLogMapper.getCount(year, month);
                MesProcessReleaseLog mesProcessReleaseLog = MesProcessReleaseLog
                        .builder()
                        .version("v" + (year - 2023) + "." + month + "." + (count + 1) + "_" + DateUtils.parseDateToStr("yyyyMMdd", date))
                        .releaseDimension("新增")
                        .releaseContent(content)
                        .status("0")
                        .createTime(new Date())
                        .updateTime(new Date())
                        .year(year)
                        .month(month)
                        .build();
                boolean result = mesProcessReleaseLogMapper.insertMesProcessReleaseLog(mesProcessReleaseLog);
                if (result) {
                    mesProjectRecords.forEach(value -> {
                        value.setVersion(mesProcessReleaseLog.getVersion());
                        MesProjectRecordsMapper.updateMesProjectRecords(value);
                    });
                } else {
                    return false;
                }
            }
        }
        return true;
    }

}
