package com.test.dubbo.myserviceprovider.dubbo;

import com.test.dubbo.myserviceapi.user.UserService;
import com.test.dubbo.myserviceapi.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public User addUser(String name) {
        log.info("收到客户端的调用={}", name);
        return new User(name, "desc" + name, new Double(Math.random()*100).intValue());
    }
}
