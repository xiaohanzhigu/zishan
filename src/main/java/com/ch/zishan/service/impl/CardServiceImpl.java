package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.mapper.CardMapper;
import com.ch.zishan.mapper.ChapterMapper;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Resource
    private CardMapper cardMapper;

    @Resource
    private CardGroupMapper cardGroupMapper;

    @Resource
    private ChapterMapper chapterMapper;


    @Override
    @Transactional
    public boolean addCard(Card card) {
        cardMapper.insert(card);
        if (card.getType() == 9) {
            return true;
        }
        // 更新章节和卡片组的卡片数量
        Chapter chapter = chapterMapper.selectById(card.getChapter());
        chapter.setCardTotal(chapter.getCardTotal() + 1);
        chapterMapper.updateById(chapter);

        CardGroup cardGroup = cardGroupMapper.selectById(chapter.getCardGroup());
        cardGroup.setCardTotal(cardGroup.getCardTotal() + 1);
        cardGroupMapper.updateById(cardGroup);

        return true;
    }

    @Override
    public Integer recoverCard(Long chapterId) {
        return cardMapper.updateIsDeleted(chapterId);
    }
}
