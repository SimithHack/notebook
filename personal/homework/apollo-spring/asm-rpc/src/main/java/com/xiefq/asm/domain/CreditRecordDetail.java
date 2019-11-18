package com.xiefq.asm.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 信用记录详细
 */
@Data
public class CreditRecordDetail {
    /**
     * 认证名称
     */
    private String authName;
    /**
     * 社会统一信用代码
     */
    private String socialCreditCode;
    /**
     * 业务范围名称
     */
    private String areaName;
    /**
     * 信用额度
     */
    private Float creditQuota;
    /**
     * 可用额度占比
     */
    private Float ratio;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 客户详细信息列表
     */
    private List<CustomerDetail> details;

    /**
     * 客户详细
     */
    @Data
    public static class CustomerDetail {
        /**
         * 客户名称
         */
        private String customerName;
        /**
         * 客户id
         */
        private Long customerId;
        /**
         * 未回款金额
         */
        private Float unPayback;
        /**
         * 占信用额度比例
         */
        private Float ratio;
    }
}
