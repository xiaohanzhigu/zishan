package com.ch.zishan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.zishan.pojo.CardGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CardGroupMapper extends BaseMapper<CardGroup> {

    @Select("select * from tb_card_group where user = #{userId} and is_deleted = 1")
    public List<CardGroup> getAllByUserIdDeletedXml(Long userId);

    @Update("update tb_card_group set is_deleted = 0 where id = #{id}")
    public Integer updateIsDeleted(Long id);
}
