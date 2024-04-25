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

@TableName("tb_chapter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    private Integer id;
    private String name;
    private CardGroup cardGroup;
    private Integer total;
    private Integer isDeleted;

    @TableField(exist = false)
    private List<Card> cardList;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    public void setCardGroup(Integer cardGroupId) {
        this.cardGroup = new CardGroup();
        this.cardGroup.setId(cardGroupId);
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}