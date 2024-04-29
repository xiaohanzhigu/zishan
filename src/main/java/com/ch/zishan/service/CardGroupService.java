package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.CardGroup;

public interface CardGroupService extends IService<CardGroup> {

    public Integer addCardGroup(CardGroup cardGroup);

    public boolean deleteCardGroup(Long id);

    public Integer recoverCardGroup(Long id);

    public Integer deleteOrRecoverCardGroupLogic(Long id, Integer isDeleted);

}
