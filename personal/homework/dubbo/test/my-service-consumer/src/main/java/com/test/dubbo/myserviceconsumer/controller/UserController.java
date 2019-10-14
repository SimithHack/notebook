package com.test.dubbo.myserviceconsumer.controller;

import com.test.dubbo.myserviceapi.user.UserService;
import com.test.dubbo.myserviceapi.user.domain.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户API
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference(
            url = "dubbo://localhost:12345"
    )
    private UserService userService;

    @RequestMapping("/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name){
        return ResponseEntity.ok(userService.addUser(name));
    }
}
