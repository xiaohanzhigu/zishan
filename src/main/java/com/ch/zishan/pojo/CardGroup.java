package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@TableName("tb_card_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;
    private String name;
    private Long user;
    private Integer collection;
    private Integer chapterTotal = 0;
    private Integer cardTotal = 0;
    private Integer isPublic;
    private Integer isDeleted;

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
}
