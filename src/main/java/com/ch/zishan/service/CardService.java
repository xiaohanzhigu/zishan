package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Card;
import org.springframework.stereotype.Service;

@Service
public interface CardService extends IService<Card> {

    public boolean addCard(Card card);

    public Integer deleteOrRecoverCardLogic(Long cardId, Integer isDeleted);

    public Integer deleteCard(Long cardId);
}
