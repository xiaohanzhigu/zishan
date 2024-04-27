package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.exception.ServiceException;
import com.ch.zishan.mapper.UserMapper;
import com.ch.zishan.pojo.User;
import com.ch.zishan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ch.zishan.utils.TokenUtils;

import javax.annotation.Resource;
@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;
    // 验证用户账户是否合法
    public User login(User user) {
//        User dbUser = selectByUsername(user.getUsername());
        User dbUser = getOne(new QueryWrapper<User>().eq("username",user.getUsername()));
        if (dbUser == null) {
            // 抛出一个自定义的异常
            throw new ServiceException("用户名或密码错误");
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        // 生成token
        String token = TokenUtils.createToken(dbUser.getId().toString(), dbUser.getPassword());
        dbUser.setToken(token);
        log.info("用户"+user.getUsername()+"的token为"+token);
        return dbUser;
    }

    @Override
    public User register(User user) {
        return null;
    }

    @Override
    public void resetPassword(User user) {

    }

}
