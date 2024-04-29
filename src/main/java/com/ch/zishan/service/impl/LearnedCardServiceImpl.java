package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.*;
import com.ch.zishan.service.LearnedCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@Transactional
public class LearnedCardServiceImpl extends ServiceImpl<LearnedCardMapper, LearnedCard> implements LearnedCardService {

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
    public List<LearnedCard> getLearnedCardList(Long learnedCardGroupId, int num) {
        QueryWrapper<LearnedCard> learnedCardQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardQueryWrapper = new QueryWrapper<>();

        learnedCardQueryWrapper.eq("learned_card_group_id", learnedCardGroupId)
                .eq("masterDegree",0)
                .last("limit 1");
        List<LearnedCard> learnedCardList = learnedCardMapper.selectList(learnedCardQueryWrapper);
        learnedCardList.forEach(learnedCard -> {
            cardQueryWrapper.clear();
            cardQueryWrapper.eq("id",learnedCard.getCardId());
            Card card = cardMapper.selectOne(cardQueryWrapper);
            learnedCard.setCard(card);
        });
        learnedCardList = learnedCardList.subList(0, num);
        return learnedCardList;
    }

    @Override
    public Integer deleteOrRecoverLearnedCardLogic(Long cardId, Integer isDeleted) {
        UpdateWrapper<LearnedCard> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("card_id", cardId).set("is_deleted", isDeleted);
        return learnedCardMapper.update(null, updateWrapper);
    }


}
