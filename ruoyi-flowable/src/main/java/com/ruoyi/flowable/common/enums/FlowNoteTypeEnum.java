package com.ruoyi.flowable.common.enums;

public enum FlowNoteTypeEnum {

    START_EVENT("START_EVENT","开始节点"),
    END_EVENT("END_EVENT","结束节点"),
    ACTIVITY("ACTIVITY","任务"),
    GATEWAY("GATEWAY","网关"),
    First_Level_Audit("GATEWAY","一级审核"),
    Second_Level_Audit("GATEWAY","二级审核"),
    Third_Level_Audit("GATEWAY","三级审核"),
    Release_Start("GATEWAY","发布开始"),
    Release_End("GATEWAY","发布结束");

    private final String code;

    private final String desc;

    FlowNoteTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
