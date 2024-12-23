package com.ruoyi.flowable.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.flowable.mybatis.mapper.BaseMapperWithHist;
import com.ruoyi.flowable.mybatis.service.IServiceWithHist;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yuji
 * @date 2022/1/22 11:51
 */
public class ServiceWithHistImpl<M extends BaseMapperWithHist<T>, T> extends ServiceImpl<M, T> implements IServiceWithHist<T> {

    @Autowired
    protected M baseMapperWithHist;

    @Override
    public BaseMapperWithHist<T> getBaseMapperWithHist() {
        return baseMapperWithHist;
    }
}
