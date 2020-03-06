package com.xiefq.asm.core.dao;


import com.xiefq.asm.core.entity.CreditCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 信用额度客户信息dao
 */
public interface CreditCustomerDao extends JpaRepository<CreditCustomer, Long> {
    /**
     * 查询认证额度下相关联的所有客户
     * @param id
     * @return
     */
    List<CreditCustomer> findByCreditRecordId(Long id);
}
