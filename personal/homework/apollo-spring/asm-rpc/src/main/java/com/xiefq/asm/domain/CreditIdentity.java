package com.xiefq.asm.domain;

import lombok.Data;

/**
 * 信用额度身份识别
 */
@Data
public class CreditIdentity {
    /**
     * pinId
     */
    private String jumaPin;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 业务区域编码
     */
    private String areaCode;
    /**
     * 客户id
     */
    private String customerId;
}
