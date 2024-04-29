package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.LearnedCardGroup;

public interface LearnedCardGroupService extends IService<LearnedCardGroup> {

    public void addCardGroupToLearnedCardGroup(Long userId, Long cardGroupId);

    public Integer deleteOrRecoverLearnedCardGroupLogic(Long cardGroupId, Integer isDeleted);

}
