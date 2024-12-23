package com.ruoyi.flowable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.flowable.domain.dto.MesDictData;

import java.util.List;

public interface IMesDictDataService extends IService<MesDictData> {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    MesDictData selectMesDictDataById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param mesDictData 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<MesDictData> selectMesDictDataList(MesDictData mesDictData);

    /**
     * 新增【请填写功能名称】
     *
     * @param mesDictData 【请填写功能名称】
     * @return 结果
     */
    boolean insertMesDictData(MesDictData mesDictData);

    /**
     * 修改【请填写功能名称】
     *
     * @param mesDictData 【请填写功能名称】
     * @return 结果
     */
    boolean updateMesDictData(MesDictData mesDictData);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    boolean deleteMesDictDataByIds(List<Long> ids);

    /**
     * 通过module和dict_code查询对应值
     * @param mesDictData 字典对象
     * @return 字典值
     * */
    MesDictData getDictValueByKey(MesDictData mesDictData);
    MesDictData getDictValueByKeyDept(MesDictData mesDictData);

    MesDictData distinct();

}
