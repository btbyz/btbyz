package com.ruoyi.flowable.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author yuji
 * @date 2022/1/22 11:12
 */
public interface BaseMapperWithHist<T> extends BaseMapper<T> {

    /**
     * 根据主键备份至历史表
     * @param entity
     * @return
     */
    int copyToHistory(T entity);

    /**
     * 将数据存入历史表
     * @param entity
     * @return
     */
    int insertIntoHist(T entity);

    /**
     * 根据主键删除历史表数据
     * @param entity
     * @return
     */
    int deleteByIdFromHistory(T entity);

    /**
     * 通过主键查询历史表
     * @param entity
     * @return
     */
    T selectByIdFromHistory(T entity);

    /**
     * 通过主键查询历史表
     * @param entity
     * @return
     */
    List<T> selectListFromHistory(T entity);

    /**
     * 通过主键查询数量
     * @param entity
     * @return
     */
    int countById(T entity);

    /**
     * 复合主键更新插入
     * */
     default int insertOrUpdateByMultiId(T entity) {
        int count = this.countById(entity);
        if ( count == 0 ) {
            return this.insert(entity);
        } else {
            return this.updateById(entity);
        }
    }

}
