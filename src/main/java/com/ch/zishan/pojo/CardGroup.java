package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@TableName("tb_card_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardGroup {
    private Integer id;
    private String name;
    private User user;
    private Integer total;
    private Integer collection;
    private Integer isPublic;
    private Integer isDeleted;

    @TableField(exist = false)
    private Integer cardTotal = 0;

    @TableField(exist = false)
    private List<Chapter> chapterList;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }

    public void setUser(Integer userId) {
        User user = new User();
        user.setId(userId);
    }

}
