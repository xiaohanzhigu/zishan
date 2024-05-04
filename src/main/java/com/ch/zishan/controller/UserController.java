package com.ch.zishan.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.exception.ServiceException;
import com.ch.zishan.interceptor.AuthAccess;
import com.ch.zishan.pojo.User;
import com.ch.zishan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/logout")
    public Result<String> logout() {
        BaseContext.remove();
        return Result.success("退出成功");
    }

    @AuthAccess
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        log.info("用户注册:" + user.toString());
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }

        if (userService.selectByUsername(user.getUsername()) != null) {
            return Result.error("404", "用户名已存在");
        }
        user.setNickname(user.getUsername());
        user = userService.register(user);

        if (user == null) {
            return Result.error("404", "注册失败");
        }
        return Result.success(user);
    }

    @AuthAccess
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        log.info("用户登录:" + user.getUsername());
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        BaseContext.set(user.getId());
        return Result.success(user);
    }

    @GetMapping
    public Result<User> getUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        log.info("查询用户"+token);
        long userId;
        try {
            userId = Long.parseLong(JWT.decode(token).getAudience().get(0));
        }  catch (JWTDecodeException e) {
            throw new ServiceException("401","请登录");
        }
        User user = userService.getById(userId);
        return Result.success(user);
    }

}
