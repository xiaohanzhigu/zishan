package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.CardGroup;

import java.util.List;

public interface CardGroupService extends IService<CardGroup> {

    public Integer addCardGroup(CardGroup cardGroup);

    public boolean deleteCardGroup(Long id);

    public List<CardGroup> getDeleted(Long userId);

    public Integer recoverCardGroup(Long id);


}
