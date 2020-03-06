package com.xiefq.asm.core.dao;


import com.xiefq.asm.core.entity.CreditQuotaAdjustRecordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 信用额度调整记录dao
 */
public interface CreditQuotaAdjustRecordEntityDao extends CrudRepository<CreditQuotaAdjustRecordEntity, Long> {
    /**
     * 查找给定客户+业务范围的额度调整记录
     * @param creditRecordId
     * @return
     */
    List<CreditQuotaAdjustRecordEntity> findByCreditRecordId(Long creditRecordId);
}
