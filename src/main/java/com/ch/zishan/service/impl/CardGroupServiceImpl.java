package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.mapper.CardMapper;
import com.ch.zishan.mapper.ChapterMapper;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CardGroupServiceImpl extends ServiceImpl<CardGroupMapper, CardGroup> implements CardGroupService {
    @Resource
    private CardGroupMapper cardGroupMapper;
    @Resource
    private CardMapper cardMapper;
    @Resource
    private ChapterMapper chapterMapper;

    public Integer addCardGroup(CardGroup cardGroup) {
        int id = cardGroupMapper.insert(cardGroup);

        Chapter chapter = new Chapter();
        chapter.setName("无标题章节");
        chapter.setCardGroup(cardGroup.getId());
        chapterMapper.insert(chapter);

        Card card = new Card();
        card.setType(9);
        card.setContent("占位卡片");
        card.setHeadline("占位卡片");
        card.setChapter(chapter.getId());
        cardMapper.insert(card);
        return id;
    }

}
