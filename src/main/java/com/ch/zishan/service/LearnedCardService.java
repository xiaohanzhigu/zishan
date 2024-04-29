package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.LearnedCard;

import java.util.List;

public interface LearnedCardService extends IService<LearnedCard> {
    public List<LearnedCard> getLearnedCardList(Long learnedCardGroupId, int num);

    public Integer deleteOrRecoverLearnedCardLogic(Long cardId, Integer isDeleted);

}
