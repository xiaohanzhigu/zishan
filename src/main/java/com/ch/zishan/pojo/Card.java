package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@TableName("tb_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long chapter;
    private String content;
    private String headline;
    private Integer type;
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}

