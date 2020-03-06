package com.xiefq.asm.core.service.rpcimpl;

import com.xiefq.asm.cnst.QuotaSourceType;
import com.xiefq.asm.core.dao.CreditCustomerDao;
import com.xiefq.asm.core.dao.CreditQuotaAdjustRecordEntityDao;
import com.xiefq.asm.core.dao.CreditRecordEntityDao;
import com.xiefq.asm.core.domain.LoginUser;
import com.xiefq.asm.core.entity.CreditCustomer;
import com.xiefq.asm.core.entity.CreditQuotaAdjustRecordEntity;
import com.xiefq.asm.core.entity.CreditRecordEntity;
import com.xiefq.asm.domain.*;
import com.xiefq.asm.rpc.CreditService;
import com.xiefq.auth.authority.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 信用额度RPC接口实现
 */
@Service
@Component
@Slf4j
public class CreditServiceImpl implements CreditService {
    /**
     * 基础额度（初始额度）
     */
    @Value("${init.base-quota}")
    private Float baseQuota;

    @Autowired
    private CreditRecordEntityDao crDao;

    @Autowired
    private CreditCustomerDao ccDao;

    @Autowired
    private CreditQuotaAdjustRecordEntityDao crlogDao;

    @Reference(version = "*")
    private AuthorityService authorityService;
    /**
     * 创建信用额度
     * @param identity
     * @return
     */
    @Override
    @Transactional
    public CreditRecord create(CreditIdentity identity) {
        LoginUser user = authorityService.loadLoginUser("3609988A50BB412D9AEFC0318C2D272C");
        log.info("user info {}", user);
        //检查参数完整性
        checkIdentity(identity);
        //检查是否已经存在
        CreditRecordEntity dbCr = crDao.byIdentity(identity);
        if(null==dbCr){
            CreditRecordEntity cr = new CreditRecordEntity();
            cr.setAreaCode(identity.getAreaCode());
            cr.setCreateDate(new Date());
            cr.setUnPaybackQuota(0F);
            cr.setRatio(1F);
            cr.setAvailableQuota(this.baseQuota);
            cr.setJumaPin(identity.getJumaPin());
            cr.setQuota(this.baseQuota);
            cr.setTenantId(identity.getTenantId());
            //TODO: 获取认证名称等信息
            crDao.save(cr);
            //添加调整记录
            CreditQuotaAdjustRecordEntity crlog = new CreditQuotaAdjustRecordEntity();
            crlog.setBeforeQuota(0F);
            crlog.setAfterQuota(cr.getQuota());
            //TODO: 获取当前登录用户
            crlog.setCause("");
            crlog.setSource(QuotaSourceType.INITIAL);
            crlog.setCreateDate(new Date());
            crlog.setCreditRecordId(cr.getId());
            crlogDao.save(crlog);
            dbCr = cr;
        }
        CreditRecord rtnCr = new CreditRecord();
        BeanUtils.copyProperties(dbCr, rtnCr);
        return rtnCr;
    }

    /**
     * 签约
     * @param identity
     * @param quota
     * @return
     */
    @Override
    @Transactional
    public CreditRecord sign(CreditIdentity identity, Float quota) {
        //更新额度
        CreditRecordEntity record = updateQuota(identity, quota, QuotaSourceType.SINGN_CONTACT, QuotaSourceType.SINGN_CONTACT.getDes());
        CreditRecord rtnCr = new CreditRecord();
        BeanUtils.copyProperties(record, rtnCr);
        return rtnCr;
    }

    /**
     * 额度手动调整
     * @param identity
     * @param quota
     * @param cause
     * @return
     */
    @Override
    @Transactional
    public CreditRecord adjust(CreditIdentity identity, Float quota, String cause) {
        //更新额度
        CreditRecordEntity record = updateQuota(identity, quota, QuotaSourceType.MANUAL, cause);
        //返回结果
        CreditRecord rtnCr = new CreditRecord();
        BeanUtils.copyProperties(record, rtnCr);
        return rtnCr;
    }

    /**
     * 获取信用额度调整记录
     * @param identity
     * @return
     */
    @Override
    public List<AdjustRecord> getAdjustRecords(CreditIdentity identity) {
        CreditRecordEntity dbCr = crDao.byIdentity(identity);
        List<AdjustRecord> rtnRecords = new ArrayList<>();
        if(dbCr!=null){
            List<CreditQuotaAdjustRecordEntity> logs  = crlogDao.findByCreditRecordId(dbCr.getId());
            for(CreditQuotaAdjustRecordEntity log : logs){
                AdjustRecord ar = new AdjustRecord();
                BeanUtils.copyProperties(log, ar);
                rtnRecords.add(ar);
            }
        }
        return rtnRecords;
    }

    /**
     * 业务范围变更-触发更新
     *  检查是否存在授信，有就共享，没有就创建一个新的信用额度记录
     * @param identity
     * @return
     */
    @Override
    public CreditRecord updateRecord(CreditIdentity identity) {
        CreditRecordEntity dbCr = crDao.byIdentity(identity);
        if(null != dbCr){
            //不存在授信记录，就重新创建一条新记录
            return create(identity);
        }else{
            //存在已有记录-直接返回
            CreditRecord rtnCr = new CreditRecord();
            BeanUtils.copyProperties(dbCr, rtnCr);
            return rtnCr;
        }
    }

    @Override
    public boolean setUnPayback(CreditIdentity identity, Float unPayBackQuota) {
        return false;
    }

    @Override
    public PageResponse<CreditRecord> search(CreditRecordQuery recordQuery) {
        Specification spec = (Specification<CreditRecordEntity>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            //业务范围
            if(!StringUtils.isEmpty(recordQuery.getAreaCode())){
                predicates.add(criteriaBuilder.equal(root.get("areaCode"), recordQuery.getAreaCode()));
            }
            //额度占比
            if(recordQuery.getBtRatio()!=null && recordQuery.getLtRatio()!=null && recordQuery.getLtRatio()>recordQuery.getBtRatio()){
                predicates.add(criteriaBuilder.between(root.get("ratio"), recordQuery.getBtRatio(), recordQuery.getBtRatio()));
            }
            //认证名称
            if(!StringUtils.isEmpty(recordQuery.getAuthName())){
                predicates.add(criteriaBuilder.like(root.get("authName"), "%"+recordQuery.getAuthName()+"%"));
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
        Pageable pageable = new QPageRequest(recordQuery.getPageStart()/recordQuery.getPageSize(), recordQuery.getPageSize());
        Page<CreditRecordEntity> records = crDao.findAll(spec,pageable);
        List<CreditRecord> data = new ArrayList<>(records.getSize());
        records.forEach((e -> {
            CreditRecord c = new CreditRecord();
            BeanUtils.copyProperties(e, c);
            data.add(c);
        }));
        //分页信息返回
        PageResponse<CreditRecord> rtn = new PageResponse<>();
        rtn.setData(data);
        rtn.setPageSize(recordQuery.getPageSize());
        rtn.setPageStart(recordQuery.getPageStart());
        rtn.setTotal(records.getTotalElements());
        return rtn;
    }

    @Override
    public CreditRecordDetail detail(CreditIdentity identity) {
        //查询认证主信息
        CreditRecordEntity mainInfo = crDao.byIdentity(identity);
        if(null != mainInfo){
            //返回信息
            CreditRecordDetail detail = new CreditRecordDetail();
            detail.setAuthName(mainInfo.getAuthName());
            detail.setCreateDate(mainInfo.getCreateDate());
            detail.setRatio(mainInfo.getRatio());
            detail.setAreaName(mainInfo.getAreaCode());
            detail.setCreditQuota(mainInfo.getQuota());
            detail.setSocialCreditCode(mainInfo.getSocialCode());
            //查询客户信息
            List<CreditCustomer> customers = ccDao.findByCreditRecordId(mainInfo.getId());
            List<CreditRecordDetail.CustomerDetail> cds = new ArrayList(customers.size());
            for(CreditCustomer c : customers){
                CreditRecordDetail.CustomerDetail cd = new CreditRecordDetail.CustomerDetail();
                cd.setCustomerId(c.getCustomerId());
                cd.setCustomerName(c.getCustomerName());
                cd.setRatio(c.getRatio());
                cd.setUnPayback(c.getUnPayback());
                cds.add(cd);
            }
            detail.setDetails(cds);
            return detail;
        }
        return null;
    }

    @Override
    public OutputStream export(CreditRecordQuery query) {

        return null;
    }

    @Override
    public int impot(InputStream ins) {
        return 0;
    }

    /**
     * 检查授信额度唯一身份识别参数的完整性
     * @param identity
     * @return
     */
    private void checkIdentity(CreditIdentity identity){
        if(StringUtils.isEmpty(identity.getJumaPin())){
            throw new RuntimeException("pinId is null");
        }
        if(StringUtils.isEmpty(identity.getCustomerId())){
            throw new RuntimeException("customerId is null");
        }
        if(StringUtils.isEmpty(identity.getAreaCode())){
            throw new RuntimeException("areaCode is null");
        }
        if(StringUtils.isEmpty(identity.getTenantId())){
            throw new RuntimeException("tenantId is null");
        }
    }

    /**
     * 更新额度
     * @param identity
     * @param quota
     * @return
     */
    private CreditRecordEntity updateQuota(CreditIdentity identity, Float quota, QuotaSourceType type, String cause){
        checkIdentity(identity);
        CreditRecordEntity dbCr = crDao.byIdentity(identity);
        if(null != dbCr){
            Float oldQuota = dbCr.getQuota();
            dbCr.setQuota(quota);
            //更新可用和比例
            dbCr.setAvailableQuota(quota-dbCr.getUnPaybackQuota());
            dbCr.setRatio(new BigDecimal(dbCr.getAvailableQuota()).divide(new BigDecimal(dbCr.getQuota())).setScale(2, RoundingMode.CEILING).floatValue());
            crDao.updateQuota(dbCr);
            //记录调整日志
            saveLog(dbCr, type, oldQuota, cause);
            return dbCr;
        }else{
            throw new RuntimeException("不能存在此额度信息");
        }
    }

    /**
     * 保存额度调整记录
     * @return
     */
    private CreditQuotaAdjustRecordEntity saveLog(CreditRecordEntity record, QuotaSourceType type, Float oldQuota, String cause){
        //历史记录
        CreditQuotaAdjustRecordEntity crLog = new CreditQuotaAdjustRecordEntity();
        crLog.setCreateDate(new Date());
        crLog.setSource(type);
        crLog.setCreditRecordId(record.getId());
        crLog.setCause(cause);
        crLog.setBeforeQuota(oldQuota);
        crLog.setAfterQuota(record.getQuota());
        crlogDao.save(crLog);
        return crLog;
    }
}
