package com.xiefq.asm.cnst;

/**
 * 额度告警类型
 */
public enum QuotaAlarmType {
    WARNING("预警提醒"),
    DANGER("风险提醒"),
    RESET_DANGER("风险解除提醒");

    String des;
    QuotaAlarmType(String des){
        this.des = des;
    }
}
