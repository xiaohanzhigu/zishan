package com.ch.zishan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.LearnedCardGroup;
import com.ch.zishan.service.CardGroupService;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.LearnedCardGroupService;
import com.ch.zishan.service.LearnedCardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/learnedCardGroup")
public class LearnedCardGroupController {
    @Resource
    private LearnedCardGroupService learnedCardGroupService;
    @Resource
    private LearnedCardService learnedCardService;
    @Resource
    private CardService cardService;
    @Resource
    private CardGroupService cardGroupService;

    @GetMapping("/dayPlan")
    public Result<Integer> getDayPlanNum(@RequestParam Long cardGroupId) {
        log.info("获取今日计划学习数量");
        CardGroup group = cardGroupService.getById(cardGroupId);
        QueryWrapper<LearnedCardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("card_group_id", cardGroupId)
                .eq("user_id", BaseContext.get())
                .eq("is_deleted", 0);

        LearnedCardGroup learnedGroup = learnedCardGroupService.getOne(wrapper);
        if (learnedGroup == null) {
            return Result.error("卡片集不存在");
        }

        return Result.success(learnedGroup.getDayPlanNum(),String.valueOf(group.getCardTotal()));
    }

    @PutMapping("/dayPlan")
    public Result<String> setDayPlanNum(@RequestBody Map<String,Object> map) {
        log.info("设置今日计划学习数量" + map);
        long cardGroupId = Long.parseLong(map.get("cardGroupId").toString());
        int dayPlan = Integer.parseInt(map.get("dayPlan").toString());

        CardGroup group = cardGroupService.getById(cardGroupId);
        QueryWrapper<LearnedCardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("card_group_id", cardGroupId)
                .eq("user_id", BaseContext.get())
                .eq("is_deleted", 0);

        LearnedCardGroup learnedGroup = learnedCardGroupService.getOne(wrapper);
        if (learnedGroup == null) {
            return Result.error("卡片集不存在");
        }
        if (dayPlan > group.getCardTotal() || dayPlan < 0) {
            return Result.error("超出范围");
        }
        learnedGroup.setDayPlanNum(dayPlan);
        learnedCardGroupService.updateById(learnedGroup);
        return Result.success("设置成功");
    }

    @PostMapping("/finishLearn")
    public Result<String> finishLearn(@RequestBody Map<String,Object> map) {
        log.info("完成学习" + map);
        long cardId = Long.parseLong(map.get("cardId").toString());
        long groupId = Long.parseLong(map.get("cardGroupId").toString());
        String firstClick = map.get("firstClick").toString();
        Card card = cardService.getById(cardId);
        if (card == null) {
            return Result.error("卡片不存在");
        }
        CardGroup group = cardGroupService.getById(groupId);
        if (group == null) {
            return Result.error("卡片集不存在");
        }
        if (StringUtils.isBlank(firstClick)) {
            return Result.error("请求错误");
        }
        learnedCardService.finishLearnOrReview(groupId,cardId, firstClick);

        return Result.success("学习完成");
    }

    @GetMapping("/learningCardList")
    public Result<List<Card>> getTodayLearningCardList(@RequestParam Long cardGroupId) {
        log.info("获取今日学习卡片列表");
        QueryWrapper<LearnedCardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("card_group_id", cardGroupId)
                .eq("user_id", BaseContext.get())
                .eq("is_deleted", 0);

        LearnedCardGroup learnedGroup = learnedCardGroupService.getOne(wrapper);
        if (learnedGroup == null) {
            return Result.error("卡片集不存在");
        }
        List<Card> cardList = learnedCardService.getLearnedCardList(learnedGroup);
        if (cardList == null || cardList.isEmpty()) {
            return Result.success("201", "该卡片集已学习完毕");
        }
        return Result.success(cardList);
    }
}
