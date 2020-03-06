package com.xiefq.asm.domain;

import lombok.Data;

import java.util.List;

/**
 * 分页响应
 */
@Data
public class PageResponse<T> {
    /**
     * 总数
     */
    private long total;
    /**
     * 分页开始位置
     */
    private int pageStart;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 数据
     */
    private List<T> data;
}
