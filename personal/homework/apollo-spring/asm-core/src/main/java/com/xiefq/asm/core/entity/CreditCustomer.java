package com.xiefq.asm.core.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 信用额度客户信息
 */
@Entity
@Data
@Table(name="credit_customer")
public class CreditCustomer {
    /**
     * 客户id
     */
    @Id
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 未回款金额
     */
    private Float unPayback;
    /**
     * 占授信额度比例
     */
    private Float ratio;
    /**
     * 信用额度记录id
     */
    private Long creditRecordId;
}
