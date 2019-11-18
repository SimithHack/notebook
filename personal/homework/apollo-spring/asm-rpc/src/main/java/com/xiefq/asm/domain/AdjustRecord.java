package com.xiefq.asm.domain;

import com.xiefq.asm.cnst.QuotaSourceType;
import lombok.Data;

import java.util.Date;

/**
 * 额度调整记录
 */
@Data
public class AdjustRecord {
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
    private String cause;
    /**
     * 来源
     */
    private QuotaSourceType source;
    /**
     * 更改日期
     */
    private Date createDate;
}
