package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CardGroupServiceImpl extends ServiceImpl<CardGroupMapper, CardGroup> implements CardGroupService {
    @Resource
    private CardGroupMapper cardGroupMapper;
    @Resource
    private CardService cardService;
    @Resource
    private ChapterService chapterService;
    @Resource
    private LearnedCardGroupService learnedCardGroupService;
    @Resource
    private LearnedCardService learnedCardService;

    @Override
    public Integer recoverCardGroup(Long cardGroupId) {
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardQueryWrapper = new QueryWrapper<>();

        // 恢复卡片集和对应的学习卡片集
        this.deleteOrRecoverCardGroupLogic(cardGroupId, 0);
        learnedCardGroupService.deleteOrRecoverLearnedCardGroupLogic(cardGroupId, 0);
        // 恢复卡片集中的所有章节
        chapterService.deleteOrRecoverChapterLogic(cardGroupId, 0);
        // 恢复每个章节中的卡片和对应的学习卡片
        chapterWrapper.eq("card_group",cardGroupId);
        chapterService.list(chapterWrapper).forEach(chapter -> {
            cardQueryWrapper.clear();
            cardQueryWrapper.eq("chapter",chapter.getId());
            cardService.list(cardQueryWrapper).forEach(card -> {
                cardService.deleteOrRecoverCardLogic(card.getId(),0);
                learnedCardService.deleteOrRecoverLearnedCardLogic(card.getId(),0);
            });
       });
        return 1;
    }

    public Integer addCardGroup(CardGroup cardGroup) {
        int id = cardGroupMapper.insert(cardGroup);

        // 插入一个默认章节
        Chapter chapter = new Chapter();
        chapter.setName("无标题章节");
        chapter.setCardGroup(cardGroup.getId());
        chapterService.addChapter(chapter);

        // 将卡片集加入到学习卡片集中
        learnedCardGroupService.addCardGroupToLearnedCardGroup(cardGroup.getUser(), cardGroup.getId());

        return id;
    }

    @Override
    public boolean deleteCardGroup(Long cardGroupId) {
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();

        // 当前卡片集内的所有章节
        chapterWrapper.eq("card_group", cardGroupId);
        List<Chapter> chapterList = chapterService.list(chapterWrapper);

        // 删除每个章节内的所有卡片和对应的学习卡片
        chapterList.forEach(chapter -> {
            cardWrapper.clear();
            cardWrapper.eq("chapter", chapter.getId());
            List<Card> cardList = cardService.list(cardWrapper);
            cardList.forEach(card -> {
                cardService.deleteOrRecoverCardLogic(card.getId(), 1);
                learnedCardService.deleteOrRecoverLearnedCardLogic(card.getId(), 1);
            });
        });

        // 删除章节
        chapterService.deleteOrRecoverChapterLogic(cardGroupId, 1);
        // 删除卡片集和对应的学习卡片集
        this.deleteOrRecoverCardGroupLogic(cardGroupId, 1);
        learnedCardGroupService.deleteOrRecoverLearnedCardGroupLogic(cardGroupId, 1);
        return true;
    }


    @Override
    public Integer deleteOrRecoverCardGroupLogic(Long id, Integer isDeleted) {
        UpdateWrapper<CardGroup> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
                .set("is_deleted", isDeleted);
        return cardGroupMapper.update(null, wrapper);
    }

}
