package com.xiefq.asm.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限登录用户
 */
@Data
public class LoginUser implements Serializable {
    private String sessionId;
    private Integer tenantId;
    private String tenantCode;
    private Integer userId;
    private String loginName;
    private String userName;
    private String mobileNumber;
}
