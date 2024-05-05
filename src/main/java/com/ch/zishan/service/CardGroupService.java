package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.CardGroup;

import java.util.List;

public interface CardGroupService extends IService<CardGroup> {

    public Integer addCardGroup(CardGroup cardGroup);

    public boolean deleteCardGroup(Long id);

    public Integer recoverCardGroup(Long id);

    public Integer deleteOrRecoverCardGroupLogic(Long id, Integer isDeleted);

    public void allDeleteCardGroup(Long id);

    public CardGroup getCardGroupById(Long id);

    public List<CardGroup> getCardGroupListByUserId(Long userId);

    public List<CardGroup> search(String key);

}
