package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.mapper.CardMapper;
import com.ch.zishan.mapper.ChapterMapper;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.ChapterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;

    @Resource
    private CardMapper cardMapper;

    @Resource
    private CardGroupMapper cardGroupMapper;

    @Resource
    private CardService cardService;

    @Override
    public boolean addChapter(Chapter chapter) {
        chapterMapper.insert(chapter);
        // 更新卡片组的章节数量
        CardGroup cardGroup = cardGroupMapper.selectById(chapter.getCardGroup());
        cardGroup.setChapterTotal(cardGroup.getChapterTotal() + 1);
        cardGroupMapper.updateById(cardGroup);

        Card card = new Card();
        card.setType(9);
        card.setContent("占位卡片");
        card.setHeadline("占位卡片");
        card.setChapter(chapter.getId());
        cardService.addCard(card);

        return true;
    }
}
