package com.test.dubbo.myserviceapi.user;


import com.test.dubbo.myserviceapi.user.domain.User;

/**
 * 用户接口
 */
public interface UserService {
    /**
     * 添加用户
     * @param name
     */
    User addUser(String name);
}
