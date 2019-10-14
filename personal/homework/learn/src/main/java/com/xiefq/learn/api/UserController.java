package com.xiefq.learn.api;

import com.xiefq.learn.entity.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @RequestMapping("/userinfo")
    public ResponseEntity<UserInfo> getUserInfo(){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername() + "@hello.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getUsername());
        userInfo.setEmail(email);
        return ResponseEntity.ok(userInfo);
    }
}
