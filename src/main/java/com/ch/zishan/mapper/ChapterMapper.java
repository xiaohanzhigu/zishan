package com.ch.zishan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.zishan.pojo.Chapter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {

    @Update("update tb_chapter set is_deleted = 0 where card_group = #{cardGroupId}")
    public Integer updateIsDeleted(Long cardGroupId);
}
