package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.*;
import com.ch.zishan.service.LearnedCardGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional
public class LearnedCardGroupServiceImpl extends ServiceImpl<LearnedCardGroupMapper, LearnedCardGroup> implements LearnedCardGroupService {

    @Resource
    private LearnedCardGroupMapper learnedCardGroupMapper;
    @Resource
    private LearnedCardMapper learnedCardMapper;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private CardGroupMapper cardGroupMapper;
    @Resource
    private ChapterMapper chapterMapper;


    @Override
    public void addCardGroupToLearnedCardGroup(Long userId, Long cardGroupId) {
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();

        CardGroup cardGroup = cardGroupMapper.selectById(cardGroupId);

        // 创建学习卡片集
        LearnedCardGroup learnedCardGroup = new LearnedCardGroup();
        learnedCardGroup.setUserId(userId);
        learnedCardGroup.setCardGroupId(cardGroupId);
        learnedCardGroup.setTotal(cardGroup.getCardTotal());
        learnedCardGroupMapper.insert(learnedCardGroup);

        //将卡片集中的卡片加入到学习卡片集中
        wrapper.eq("card_group", cardGroupId);
        List<Chapter> chapterList = chapterMapper.selectList(wrapper);
        chapterList.forEach(chapter -> {
            cardWrapper.clear();
            cardWrapper.eq("chapter", chapter.getId());
            List<Card> cardList = cardMapper.selectList(cardWrapper);
            cardList.forEach(card -> {
                LearnedCard learnedCard = new LearnedCard();
                learnedCard.setCardId(card.getId());
                learnedCard.setLearnedCardGroupId(cardGroupId);
                learnedCardMapper.insert(learnedCard);
            });
        });
    }

    @Override
    public Integer deleteOrRecoverLearnedCardGroupLogic(Long cardGroupId, Integer isDeleted) {
        UpdateWrapper<LearnedCardGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("card_group_id", cardGroupId).set("is_deleted", isDeleted);
        return learnedCardGroupMapper.update(null, updateWrapper);
    }
}
