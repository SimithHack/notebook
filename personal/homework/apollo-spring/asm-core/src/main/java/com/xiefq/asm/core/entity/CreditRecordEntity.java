package com.xiefq.asm.core.entity;

import com.xiefq.asm.cnst.QuotaSourceType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "credit_record")
@Data
public class CreditRecordEntity {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 认证名称
     */
    private String authName;

    /**
     * pinId
     */
    @Column(columnDefinition = "CHAR(32)")
    private String jumaPin;

    /**
     * 租户id
     */
    @Column(columnDefinition = "CHAR(32)")
    private String tenantId;

    /**
     * 业务区域编码
     */
    @Column(columnDefinition = "CHAR(32)")
    private String areaCode;

    /**
     * 社会信用代码
     */
    @Column(columnDefinition = "CHAR(32)")
    private String socialCode;

    /**
     * 信用额度
     */
    @Column(scale = 2)
    private Float quota;

    /**
     * 额度来源
     */
    @Column
    private QuotaSourceType source;

    /**
     * 未回款额度
     */
    @Column(scale = 2)
    private Float unPaybackQuota;

    /**
     * 额度使用占比
     */
    @Column(scale = 2)
    private Float ratio;

    /**
     * 行政区域编码
     */
    @Column(columnDefinition = "CHAR(32)")
    private String regionCode;

    /**
     * 可用信用额度
     */
    @Column(scale = 2)
    private Float availableQuota;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "TIMESTAMP")
    private Date createDate;
}
