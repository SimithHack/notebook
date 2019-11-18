package com.xiefq.asm.domain;

import lombok.Data;

/**
 * 信用记录查询dto
 */
@Data
public class CreditRecordQuery {
    /**
     * 认证名称
     */
    private String authName;
    /**
     * 业务范围编码
     */
    private String areaCode;
    /**
     * 比例下限(bt=bigger then)
     */
    private Float btRatio;
    /**
     * 比例上限(lt=less then)
     */
    private Float ltRatio;
    /**
     * 分页开始
     */
    private int pageStart = 0;
    /**
     * 页大小
     */
    private int pageSize = 20;
}
