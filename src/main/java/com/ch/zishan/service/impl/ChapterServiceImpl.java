package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.ChapterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private CardService cardService;
    @Resource
    private CardGroupMapper cardGroupMapper;
    @Resource
    private LearnedCardMapper learnedCardMapper;
    @Resource
    private LearnedCardGroupMapper learnedCardGroupMapper;

    @Override
    public boolean addChapter(Chapter chapter) {
        chapterMapper.insert(chapter);
        // 更新卡片组的章节数量
        CardGroup cardGroup = cardGroupMapper.selectById(chapter.getCardGroup());
        cardGroup.setChapterTotal(cardGroup.getChapterTotal() + 1);
        cardGroupMapper.updateById(cardGroup);

        return true;
    }

    @Override
    public Integer deleteOrRecoverChapterLogic(Long cardGroupId, Integer isDeleted) {
        UpdateWrapper<Chapter> wrapper = new UpdateWrapper<>();
        wrapper.eq("card_group", cardGroupId).set("is_deleted", isDeleted);
        return chapterMapper.update(null, wrapper);
    }

    @Override
    public Integer deleteChapter(Long id) {
        // 删除卡片集中的卡片和对应的学习计划
        QueryWrapper<Card> cardQueryWrapper = new QueryWrapper<>();
        cardQueryWrapper.eq("chapter", id);
        cardMapper.selectList(cardQueryWrapper).forEach(card -> {
            cardService.deleteCard(card.getId());
        });

        // 删除章节
        chapterMapper.deleteById(id);
        return 1;
    }
}
