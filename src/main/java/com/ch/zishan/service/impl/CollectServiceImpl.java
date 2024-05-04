package com.ch.zishan.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.mapper.CollectMapper;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Collect;
import com.ch.zishan.service.CollectService;
import com.ch.zishan.service.LearnedCardGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Resource
    private LearnedCardGroupService learnedCardGroupService;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private CardGroupMapper cardGroupMapper;


    @Override
    public void addCollect(Collect collect) {
        this.save(collect);
        // 创建学习计划
        learnedCardGroupService.addCardGroupToLearnedCardGroup(BaseContext.get(), collect.getCardGroupId());
        // 更新卡片集收藏数
        CardGroup cardGroup = cardGroupMapper.selectById(collect.getCardGroupId());
        cardGroup.setCollection(cardGroup.getCollection() + 1);
        cardGroupMapper.updateById(cardGroup);
    }

    @Override
    public void deleteCollect(Long userId, Long cardGroupId) {
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<>();
        collectQueryWrapper.eq("user_id", userId).eq("card_group_id", cardGroupId);
        this.remove(collectQueryWrapper);
        // 删除学习计划
        learnedCardGroupService.deleteLearnedCardGroup(userId, cardGroupId);
        // 更新卡片集收藏数
        CardGroup cardGroup = cardGroupMapper.selectById(cardGroupId);
        cardGroup.setCollection(cardGroup.getCollection() - 1);
        cardGroupMapper.updateById(cardGroup);
    }
}
