package com.ch.zishan.controller;

import cn.hutool.core.util.StrUtil;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.interceptor.AuthAccess;
import com.ch.zishan.pojo.User;
import com.ch.zishan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin
@RestController
public class SystemController {
    @Resource
    UserService userService;

    @AuthAccess
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        log.info("用户登录:" + user.getUsername());
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        BaseContext.set(Long.valueOf(user.getId()));
        return Result.success(user);
    }

}
