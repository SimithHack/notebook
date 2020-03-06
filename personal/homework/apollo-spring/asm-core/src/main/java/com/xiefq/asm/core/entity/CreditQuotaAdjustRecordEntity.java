package com.xiefq.asm.core.entity;

import com.xiefq.asm.cnst.QuotaSourceType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 额度调整记录
 */
@Entity
@Table(name="credit_quota_adjust_record")
@Data
public class CreditQuotaAdjustRecordEntity {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 信用额度记录表id
     */
    private Long creditRecordId;
    /**
     * 调整前的额度
     */
    private Float beforeQuota;
    /**
     * 调整后的额度
     */
    private Float afterQuota;
    /**
     * 调整原因
     */
    @Column(columnDefinition = "VARCHAR(255) DEFAULT NULL COMMENT '调整原因'")
    private String cause;
    /**
     * 来源
     */
    private QuotaSourceType source;
    /**
     * 更改日期
     */
    @Column(columnDefinition = "TIMESTAMP")
    private Date createDate;
}
