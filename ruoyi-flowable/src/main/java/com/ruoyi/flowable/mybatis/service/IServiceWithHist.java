package com.ruoyi.flowable.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ruoyi.flowable.mybatis.mapper.BaseMapperWithHist;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yuji
 * @date 2022/1/22 11:33
 */
public interface IServiceWithHist<T> extends IService<T> {

    BaseMapperWithHist<T> getBaseMapperWithHist();

    /**
     * 根据主键备份至历史表
     * @param entity
     * @return
     */
    default boolean copyToHistory(T entity) {
        return SqlHelper.retBool(this.getBaseMapperWithHist().copyToHistory(entity));
    }

    /**
     * 将数据存入历史表
     * @param entity
     * @return
     */
    default boolean insertIntoHist(T entity){
        return SqlHelper.retBool(this.getBaseMapperWithHist().insertIntoHist(entity));
    }

    /**
     * 根据主键删除历史表数据
     * @param entity
     * @return
     */
    default boolean deleteByIdFromHistory(T entity){
        return SqlHelper.retBool(this.getBaseMapperWithHist().deleteByIdFromHistory(entity));
    }

    /**
     * 通过主键查询历史表
     * @param entity
     * @return
     */
    default T selectByIdFromHistory(T entity){
        return this.getBaseMapperWithHist().selectByIdFromHistory(entity);
    }

    /**
     * 查询历史列表
     * @param entity
     * @return
     */
    default List<T> selectListFromHistory(T entity){
        return this.getBaseMapperWithHist().selectListFromHistory(entity);
    }

    /**
     * 根据主键更新实时表并备份至历史表
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    default boolean updateByIdWithHistory(T entity) {
        return this.copyToHistory(entity) && this.updateById(entity);
    }
    /**
     * 根据主键更新实时表并备份至历史表
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    default boolean updateByIdWithHistoryToCopy(T entity) {
        return this.updateById(entity) && this.copyToHistory(entity);
    }
    /**
     * 根据主键更新实时表并备份至历史表
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    default boolean batchUpdateByIdWithHistory(List<T> list) {
        list.forEach(this::updateByIdWithHistory);
        return true;
    }

    /**
     * 根据主键更新实时表并备份至历史表
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    default boolean batchInsertOrUpdateByIdWithHistory(List<T> list) {
        list.forEach(this::insertOrUpdateById);
        return true;
    }

    /**
     * 根据主键查询判断是否存在。不存在则保存至实时表，存在则保存至历史表并更新实时表
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean insertOrUpdateById(T entity) {
        int count = this.getBaseMapperWithHist().countById(entity);
        if ( count == 0 ) {
            return this.save(entity);
        } else {
            return this.updateByIdWithHistory(entity);
        }
    }

    /**
     * 根据主键查询数量
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default int countById(T entity) {
        return this.getBaseMapperWithHist().countById(entity);
    }

    /**
     * 根据主键删除实时表数据，备份至历史表
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean moveIntoHist(T entity) {
        return copyToHistory(entity) && this.removeById(entity);
    }

    /**
     * 根据主键删除实时表数据，备份至历史表
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean moveIntoHistList(List<T> list) {
        list.forEach(this::moveIntoHist);
        return true;
    }

}
