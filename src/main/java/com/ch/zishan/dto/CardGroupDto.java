package com.ch.zishan.dto;

import com.ch.zishan.pojo.CardGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardGroupDto extends CardGroup {
    private int needReviewNum;
    private int needLearnNum;
}
