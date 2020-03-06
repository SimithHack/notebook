package com.xiefq.asm.cnst;

import lombok.Getter;

public enum  QuotaSourceType {
    INITIAL("初始额度创建"),
    SINGN_CONTACT("合同签约更新"),
    MANUAL("手工调整"),
    MANUAL_IMPORT("手工调整（导入）");
    /**
     * 描述信息
     */
    @Getter
    String des;
    QuotaSourceType(String des){
        this.des = des;
    }


}
