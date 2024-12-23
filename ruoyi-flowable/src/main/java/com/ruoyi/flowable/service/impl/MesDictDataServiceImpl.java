package com.ruoyi.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.BaseErrorCodeEnum;
import com.ruoyi.common.utils.FabAssertUtils;
import com.ruoyi.flowable.domain.dto.MesDictData;
import com.ruoyi.flowable.mapper.MesDictDataMapper;
import com.ruoyi.flowable.service.IMesDictDataService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MesDictDataServiceImpl extends ServiceImpl<MesDictDataMapper, MesDictData> implements IMesDictDataService {

    @Autowired
    private MesDictDataMapper mesDictDataMapper;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired IMesDictDataService mesDictDataService;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public MesDictData selectMesDictDataById(Long id) {
        return this.getById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesDictData 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MesDictData> selectMesDictDataList(MesDictData mesDictData) {
        return mesDictDataMapper.selectMesDictDataList(mesDictData);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param mesDictData 【请填写功能名称】
     * @return 结果
     */
    @Override
    public boolean insertMesDictData(MesDictData mesDictData) {
        FabAssertUtils.isNotBlank(mesDictData.getDictCode(), BaseErrorCodeEnum.NULL_ERROR, "类型不能为空");
        FabAssertUtils.isNotBlank(mesDictData.getModule(), BaseErrorCodeEnum.NULL_ERROR, "模块不能为空");
        int count = mesDictDataMapper.count(mesDictData.getDictCode(), mesDictData.getModule());
        if (count > 0){
            FabAssertUtils.isTrue(false, BaseErrorCodeEnum.PARAM_ERROR, "已存在数据，请勿重复新增!");
        }
        return this.save(mesDictData);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param mesDictData 【请填写功能名称】
     * @return 结果
     */
    @Override
    public boolean updateMesDictData(MesDictData mesDictData) {
        return this.updateById(mesDictData);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public boolean deleteMesDictDataByIds(List<Long> ids) {
        return this.removeBatchByIds(ids);
    }

    @Override
    public MesDictData getDictValueByKey(MesDictData mesDictData) {
        if (StringUtils.isNotBlank(mesDictData.getModule()) && StringUtils.isNotBlank(mesDictData.getDictCode())) {
            QueryWrapper<MesDictData> query = new QueryWrapper<>();
            query.lambda().eq(MesDictData::getModule, mesDictData.getModule());
            query.lambda().eq(MesDictData::getDictCode, mesDictData.getDictCode());
            query.lambda().eq(MesDictData::getStatus, "A");
            return this.getOne(query);
        }
        return new MesDictData();
    }

    @Override
    public MesDictData getDictValueByKeyDept(MesDictData mesDictData) {
        if (StringUtils.isNotBlank(mesDictData.getModule()) && StringUtils.isNotBlank(mesDictData.getDictCode())) {
            Long deptId = Long.valueOf(mesDictData.getDictCode());
            SysDept sysDept = sysDeptService.selectDeptById(deptId);
            if (sysDept != null) {
                mesDictData.setDictCode(sysDept.getDeptName());
            }
            return mesDictDataService.getDictValueByKey(mesDictData);
        }
        return new MesDictData();
    }

    @Override
    public MesDictData distinct() {
        List<MesDictData> mesDictDataList = this.list();
        FabAssertUtils.isNotEmpty(mesDictDataList, BaseErrorCodeEnum.NULL_ERROR, "字典数据为空");
        MesDictData mesDictData = new MesDictData();
        Set<String> modules = new HashSet<>();
        Set<String> distCodes = new HashSet<>();
        Set<String> remarks = new HashSet<>();
        mesDictDataList.forEach(value -> {
            modules.add(value.getModule());
            distCodes.add(value.getDictCode());
            if (StringUtils.isNotBlank(value.getRemark())) {
                remarks.add(value.getRemark());
            }
        });
        mesDictData.setModules(modules);
        mesDictData.setDictCodes(distCodes);
        mesDictData.setRemarks(remarks);
        return mesDictData;
    }

}
