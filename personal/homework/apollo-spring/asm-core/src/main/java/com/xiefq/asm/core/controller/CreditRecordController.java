package com.xiefq.asm.core.controller;

import com.xiefq.asm.domain.*;
import com.xiefq.asm.rpc.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信用额度记录rest接口
 */
@RestController
@RequestMapping("/credit_records")
public class CreditRecordController {

    @Autowired
    private CreditService creditService;

    /**
     * 创建，初始化信用额度
     * @param identity
     * @return
     */
    @PostMapping
    public ResponseEntity create(CreditIdentity identity){
        CreditRecord cr = creditService.create(identity);
        return ResponseEntity.ok(cr);
    }

    /**
     * 签约
     * @param identity
     * @param quota
     * @return
     */
    @PostMapping("/sign")
    public ResponseEntity sign(CreditIdentity identity, Float quota){
        CreditRecord cr = creditService.sign(identity, quota);
        return ResponseEntity.ok(cr);
    }
    /**
     * 额度手动调整
     * @param identity 额度唯一标识
     * @param quota 调整的额度
     * @param cause 调整原因
     * @return
     */
    @PostMapping("/adjust")
    public ResponseEntity adjust(CreditIdentity identity, Float quota, String cause){
        CreditRecord cr = creditService.adjust(identity, quota, cause);
        return ResponseEntity.ok(cr);
    }
    /**
     * 获取更改记录
     * @param identity
     * @return
     */
    @GetMapping("/record_adjust_logs")
    public ResponseEntity sign(CreditIdentity identity){
        List<AdjustRecord> records = creditService.getAdjustRecords(identity);
        return ResponseEntity.ok(records);
    }

    /**
     * 业务范围变更-触发更新
     * @param identity
     * @return
     */
    @PatchMapping
    public ResponseEntity update(CreditIdentity identity){
        CreditRecord cr = creditService.updateRecord(identity);
        return ResponseEntity.ok(cr);
    }

    /**
     * 查询额度
     * @param query
     * @return
     */
    @GetMapping
    public ResponseEntity query(CreditRecordQuery query){
        PageResponse<CreditRecord> records = creditService.search(query);
        return ResponseEntity.ok(records);
    }
}
