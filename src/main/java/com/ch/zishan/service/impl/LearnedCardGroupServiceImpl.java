package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.dto.CardGroupDto;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.*;
import com.ch.zishan.service.LearnedCardGroupService;
import com.ch.zishan.utils.TimeUtils;
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
                learnedCard.setLearnedCardGroupId(learnedCardGroup.getId());
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

    @Override
    public void deleteLearnedCardGroup(Long userId, Long cardGroupId) {
        QueryWrapper<LearnedCardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("card_group_id", cardGroupId);
        LearnedCardGroup learnedCardGroup = learnedCardGroupMapper.selectOne(wrapper);
        if (learnedCardGroup == null) {
            return;
        }
        learnedCardMapper.delete(new QueryWrapper<LearnedCard>().eq("learned_card_group_id", learnedCardGroup.getId()));
        learnedCardGroupMapper.delete(wrapper);
    }

    @Override
    public CardGroupDto getReviewNumAndNotLearnedNum(Long userId, Long cardGroupId) {
        // 获取学习卡片集
        QueryWrapper<LearnedCardGroup> learnedCardGroupQueryWrapper = new QueryWrapper<LearnedCardGroup>()
                .eq("user_id", userId)
                .eq("card_group_id", cardGroupId)
                .eq("is_deleted", 0);
        LearnedCardGroup learnedCardGroup = learnedCardGroupMapper.selectOne(learnedCardGroupQueryWrapper);
        if (learnedCardGroup == null) {
            return null;
        }
        // 需要学习的卡片
        List<LearnedCard> learnedCardList = learnedCardMapper.selectList(new QueryWrapper<LearnedCard>()
                .eq("learned_card_group_id", learnedCardGroup.getId()));
        int needLearnedNum = (int) learnedCardList.stream().filter(card -> card.getMasterDegree() == 0).count();
        // 需要复习的卡片
        QueryWrapper<LearnedCard> learnedCardQueryWrapper = new QueryWrapper<>();
        learnedCardQueryWrapper.eq("learned_card_group_id",learnedCardGroup.getId())
                .le("need_review_date", TimeUtils.getTodayEndStamp())
                .eq("is_deleted", 0)
                .orderByAsc("need_review_date")
                .ge("master_degree", 1)
                .ne("deep_master_times", 3); // 深度掌握次数大于3时不再复习
        List<LearnedCard> reviewCardList = learnedCardMapper.selectList(learnedCardQueryWrapper);
        int needReviewNum = reviewCardList.size();

        CardGroupDto cardGroupDto = new CardGroupDto();
        cardGroupDto.setNeedReviewNum(needReviewNum);
        cardGroupDto.setNeedLearnNum(needLearnedNum);

        return cardGroupDto;
    }
}
