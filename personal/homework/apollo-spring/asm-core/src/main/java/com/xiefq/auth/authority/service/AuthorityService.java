package com.xiefq.auth.authority.service;
import com.xiefq.asm.core.domain.LoginUser;

/**
 * 用户中心授权接口
 */
public interface AuthorityService {
    /**
     * 加载登录用户信息，有可能是员工，也有可能是生态用户
     * 该接口不会重置session过期时间
     * @param sessionId
     * @return
     */
    LoginUser loadLoginUser(String sessionId) throws RuntimeException;
}
