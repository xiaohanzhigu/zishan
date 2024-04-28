package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.User;

public interface UserService extends IService<User> {

    public User login(User user);

    public User register(User user);

    public void resetPassword(User user);

    public User selectByUsername(String username);

}
