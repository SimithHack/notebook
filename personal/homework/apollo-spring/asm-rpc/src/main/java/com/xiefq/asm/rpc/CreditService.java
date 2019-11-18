package com.xiefq.asm.rpc;

import com.xiefq.asm.domain.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 信用额度接口
 */
public interface CreditService {
    /**
     * 创建信用额度
     * @param identity 信用额度身份参数
     * @return
     */
    CreditRecord create(CreditIdentity identity);

    /**
     * 签约
     * @param identity 信用额度身份参数
     * @param quota 账期额度值
     * @return
     */
    CreditRecord sign(CreditIdentity identity, Float quota);

    /**
     * 手动调整额度
     * @param identity 信用额度身份参数
     * @param quota 更新后的额度
     * @param cause 调整原因
     * @return
     */
    CreditRecord adjust(CreditIdentity identity, Float quota, String cause);

    /**
     * 查询某个客户的信用调整记录
     * @param identity 信用额度身份参数
     * @return
     */
    List<AdjustRecord> getAdjustRecords(CreditIdentity identity);

    /**
     * 业务范围变更-触发更新
     * 检查是否存在授信，有就共享，没有就创建一个新的信用额度记录
     * @param identity 信用额度身份参数
     * @return
     */
    CreditRecord updateRecord(CreditIdentity identity);

    /**
     * 设置未回款
     * @param identity 信用额度身份参数
     * @param unPayBackQuota 未回款额度
     * @return
     */
    boolean setUnPayback(CreditIdentity identity, Float unPayBackQuota);

    /**
     * 查询额度
     * @param query 查询信息
     * @return
     */
    PageResponse<CreditRecord> search(CreditRecordQuery query);

    /**
     * 信用记录详细
     * @param identity 信用额度身份标识
     * @return
     */
    CreditRecordDetail detail(CreditIdentity identity);

    /**
     * 导出额度列表
     * @param query 查询信息
     */
    OutputStream export(CreditRecordQuery query);

    /**
     * 导入数据
     * @param ins
     * @return 成功的条数
     */
    int impot(InputStream ins);

}
