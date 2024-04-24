package com.ch.zishan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.zishan.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
