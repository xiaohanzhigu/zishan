package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.CardGroupMapper;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardGroupService;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.ChapterService;
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

//    @Resource
//    private CardMapper cardMapper;
//
//    @Resource
//    private ChapterMapper chapterMapper;

    public Integer addCardGroup(CardGroup cardGroup) {
        int id = cardGroupMapper.insert(cardGroup);

        // 插入一个默认章节
        Chapter chapter = new Chapter();
        chapter.setName("无标题章节");
        chapter.setCardGroup(cardGroup.getId());
        chapterService.addChapter(chapter);

        return id;
    }

    @Override
    public boolean deleteCardGroup(Long id) {
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();

        // 当前卡片集内的所有章节
        chapterWrapper.eq("card_group", id);
        List<Chapter> chapterList = chapterService.list(chapterWrapper);

        chapterList.forEach(chapter -> {
            cardWrapper.clear();
            cardWrapper.eq("chapter", chapter.getId());
            List<Card> cardList = cardService.list(cardWrapper);
            // 删除每个章节内的所有卡片
            cardService.remove(cardWrapper);
            // 删除章节
            chapterService.removeById(chapter.getId());
        });
        // 删除卡片集
        cardGroupMapper.deleteById(id);

        return true;
    }

}
