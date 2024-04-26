package com.ch.zishan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.zishan.pojo.Card;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CardMapper extends BaseMapper<Card> {

    @Update("update tb_card set is_deleted = 0 where chapter = #{chapterId}")
    public Integer updateIsDeleted(Long chapterId);
}
