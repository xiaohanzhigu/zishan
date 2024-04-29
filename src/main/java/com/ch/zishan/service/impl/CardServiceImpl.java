package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.*;
import com.ch.zishan.service.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Resource
    private CardMapper cardMapper;
    @Resource
    private CardGroupMapper cardGroupMapper;
    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private LearnedCardMapper learnedCardMapper;
    @Resource
    private LearnedCardGroupMapper learnedCardGroupMapper;

    @Override
    public Integer deleteCard(Long cardId) {
        // 删除卡片和对应的学习计划
        learnedCardMapper.selectList(new QueryWrapper<LearnedCard>().eq("card_id", cardId)).forEach(learnedCard -> {
            learnedCardMapper.deleteById(learnedCard.getId());
            LearnedCardGroup learnedCardGroup = learnedCardGroupMapper.selectById(learnedCard.getLearnedCardGroupId());
            learnedCardGroup.setTotal(learnedCardGroup.getTotal() - 1);
            learnedCardGroupMapper.updateById(learnedCardGroup);
        });
        return cardMapper.deleteById(cardId);
    }


    @Override
    public boolean addCard(Card card) {
        cardMapper.insert(card);
        // 更新章节的卡片数量
        Chapter chapter = chapterMapper.selectById(card.getChapter());
        chapter.setCardTotal(chapter.getCardTotal() + 1);
        chapterMapper.updateById(chapter);

        // 更新卡片组的卡片数量
        CardGroup cardGroup = cardGroupMapper.selectById(chapter.getCardGroup());
        cardGroup.setCardTotal(cardGroup.getCardTotal() + 1);
        cardGroupMapper.updateById(cardGroup);

        // 将卡片加入到学习卡片集中
        LearnedCard learnedCard = new LearnedCard();
        learnedCard.setCardId(card.getId());
        // 将该卡片加入到该卡片组的所有学习卡片集中，包括分享出去的卡片集
        QueryWrapper<LearnedCardGroup> queryWrapper = new QueryWrapper<LearnedCardGroup>()
                .eq("card_group_id", chapter.getCardGroup());
        learnedCardGroupMapper.selectList(queryWrapper).forEach(learnedCardGroup -> {
            // 添加对应的学习计划
            learnedCard.setLearnedCardGroupId(learnedCardGroup.getId());
            learnedCardMapper.insert(learnedCard);
            // 更新学习计划的卡片数量
            learnedCardGroup.setTotal(learnedCardGroup.getTotal() + 1);
            learnedCardGroupMapper.updateById(learnedCardGroup);
        });


        return true;
    }

    @Override
    public Integer deleteOrRecoverCardLogic(Long cardId, Integer isDeleted) {
        UpdateWrapper<Card> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", cardId).set("is_deleted", isDeleted);
        return cardMapper.update(null, wrapper);
    }
}
