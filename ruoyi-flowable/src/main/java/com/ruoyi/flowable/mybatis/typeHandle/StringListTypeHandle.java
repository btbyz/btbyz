package com.ruoyi.flowable.mybatis.typeHandle;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

public class StringListTypeHandle extends ListTypeHandle<String>{
    @Override
    protected TypeReference<List<String>> specificType() {
        return new TypeReference<List<String>>() {};
    }
}
