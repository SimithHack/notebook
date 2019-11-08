package com.xiefq.asm.core.dao;


import com.xiefq.asm.core.entity.CreditRecordEntity;
import com.xiefq.asm.domain.CreditIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 信用额度记录dao
 */
public interface CreditRecordEntityDao extends JpaRepository<CreditRecordEntity, Long>, JpaSpecificationExecutor {
    /**
     * 通过信用额度的身份标识查询
     * @return
     */
    @Query("from CreditRecordEntity where jumaPin=:#{#identity.jumaPin} and customerId=:#{#identity.customerId} and areaCode=:#{#identity.areaCode} and tenantId=:#{#identity.tenantId}")
    CreditRecordEntity byIdentity(CreditIdentity identity);

    /**
     * 更新信用额度记录
     * @param record
     */
    @Modifying
    @Query("update CreditRecordEntity set quota=:#{#record.quota}, availableQuota=:#{#record.availableQuota}, ratio=:#{#record.ratio} where id=:#{#record.id}")
    void updateQuota(CreditRecordEntity record);

}
