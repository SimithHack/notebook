package com.xiefq.asm.domain;

import lombok.Data;

import java.util.Date;

/**
 * 信用额度记录
 */
@Data
public class CreditRecord {
    /**
     * 认证名称
     */
    private String authName;

    /**
     * 业务区域编码
     */
    private String areaCode;

    /**
     * juma pin
     */
    private String jumaPin;

    /**
     * 信用额度
     */
    private Float quota;

    /**
     * 额度来源
     */
    private String source;

    /**
     * 未回款额度
     */
    private Float unPaybackQuota;

    /**
     * 额度使用占比
     */
    private Float ratio;

    /**
     * 行政区域编码
     */
    private String regionCode;

    /**
     * 可用信用额度
     */
    private Float availableQuota;

    /**
     * 创建时间
     */
    private Date createDate;
}
