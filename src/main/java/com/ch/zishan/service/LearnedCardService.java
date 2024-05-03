package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.LearnedCard;
import com.ch.zishan.pojo.LearnedCardGroup;

import java.util.List;

public interface LearnedCardService extends IService<LearnedCard> {
    public List<Card> getLearnedCardList(LearnedCardGroup learnedCardGroup);

    public Integer deleteOrRecoverLearnedCardLogic(Long cardId, Integer isDeleted);

    public void finishLearnOrReview(Long cardGroupId, Long cardId, String firstClick);

}
