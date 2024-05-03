package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.mapper.*;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.LearnedCard;
import com.ch.zishan.pojo.LearnedCardGroup;
import com.ch.zishan.service.LearnedCardService;
import com.ch.zishan.service.RecordService;
import com.ch.zishan.utils.SysUtils;
import com.ch.zishan.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private RecordService recordService;

    @Override
    public List<Card> getLearnedCardList(LearnedCardGroup learnedCardGroup) {
        QueryWrapper<LearnedCard> learnedCardQueryWrapper = new QueryWrapper<>();
        List<Card> cardList = new ArrayList<>();

        learnedCardQueryWrapper.eq("learned_card_group_id", learnedCardGroup.getId())
                .eq("master_degree", 0);
        List<LearnedCard> learnedCardList = learnedCardMapper.selectList(learnedCardQueryWrapper);
        // 查出今日还需学习的数量
        int num = learnedCardGroup.getDayPlanNum() - recordService.getTodayLearnedNumByUserId(BaseContext.get());
        if (num > learnedCardList.size()) {
            num = learnedCardList.size();
        }
        learnedCardList = learnedCardList.subList(0, num);
        // 查出卡片信息
        learnedCardList.forEach(learnedCard -> {
            Card card = cardMapper.selectById(learnedCard.getCardId());
            cardList.add(card);
        });
        return cardList;
    }

    @Override
    public Integer deleteOrRecoverLearnedCardLogic(Long cardId, Integer isDeleted) {
        UpdateWrapper<LearnedCard> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("card_id", cardId).set("is_deleted", isDeleted);
        return learnedCardMapper.update(null, updateWrapper);
    }

    @Override
    public void finishLearnOrReview(Long cardGroupId, Long cardId, String firstClick) {
        Integer day = 0;
        // 查询卡片对应的卡片集
        CardGroup cardGroup = cardGroupMapper.selectById(cardGroupId);
        // 查询用户对应的学习卡片集1
        QueryWrapper<LearnedCardGroup> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("card_group_id", cardGroupId)
                .eq("user_id", BaseContext.get());
        LearnedCardGroup learnedCardGroup = learnedCardGroupMapper.selectOne(groupQueryWrapper);
        // 查询学习计划
        QueryWrapper<LearnedCard> learnedCardQueryWrapper = new QueryWrapper<>();
        learnedCardQueryWrapper.eq("learned_card_group_id", learnedCardGroup.getId())
                .eq("card_id", cardId);
        LearnedCard learnedCard = learnedCardMapper.selectOne(learnedCardQueryWrapper);

        // 更新学习计划
        if (learnedCard.getStartLearnedDate() == null) {
            learnedCard.setStartLearnedDate(TimeUtils.getCurrentTimeStamp());
        }
        learnedCard.setExamNum(learnedCard.getExamNum() + 1);
        if ("记住了".equals(firstClick)) {
            if (learnedCard.getMasterDegree() == 6) {
                learnedCard.setDeepMasterTimes(learnedCard.getDeepMasterTimes() + 1);
            } else {
                learnedCard.setMasterDegree(learnedCard.getMasterDegree() + 1);
            }
            day = SysUtils.getDegree(learnedCard.getMasterDegree());
        } else if ("模糊".equals(firstClick)) {
            day = learnedCard.getMasterDegree() == 0 ? 0 : 1;
            learnedCard.setMasterDegree(learnedCard.getMasterDegree() - 1);
        } else if ("忘记了".equals(firstClick)) {
            learnedCard.setMasterDegree(learnedCard.getMasterDegree() - 2);
            learnedCard.setDeepMasterTimes(0);
            day = 1;
        }
        if (learnedCard.getMasterDegree() <= 0) {
            learnedCard.setMasterDegree(1);
        }
        learnedCard.setNeedReviewDate(TimeUtils.getDateByDays(TimeUtils.getCurrentTimeStamp(), day));
        learnedCardMapper.updateById(learnedCard);
        recordService.dayPlanNumAddOne(BaseContext.get());
    }
}
