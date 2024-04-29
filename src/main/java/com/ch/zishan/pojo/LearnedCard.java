package com.ch.zishan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_learned_card")
public class LearnedCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.AUTO)
    private Long id;
    private Long learnedCardGroupId;
    private Long cardId;
    private Long needReviewDate;
    private Long lastReviewDate;
    private Long startLearnedDate;
    private Integer masterDegree;
    private Integer deepMasterTimes;
    private Integer examNum;
    private Integer examRightNum;
    private Integer isDeleted;

    @TableField(exist = false)
    private Card card;
}
