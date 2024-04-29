package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_learned_card_group")
public class LearnedCardGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long userId;
    private Long cardGroupId;
    private Integer total;
    private Integer learnedNum;
    private Integer reviewNum;
    private Integer dayPlanNum;
    private Integer isDeleted;

    @TableField(exist = false)
    private CardGroup cardGroup;

    @TableField(exist = false)
    private List<LearnedCard> learnedCardList;
}
