package com.ch.zishan.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ch.zishan.common.Result;
import com.ch.zishan.exception.ServiceException;
import com.ch.zishan.pojo.User;
import com.ch.zishan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

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
