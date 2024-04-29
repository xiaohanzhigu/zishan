package com.ch.zishan.controller;

import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.LearnedCard;
import com.ch.zishan.pojo.LearnedCardGroup;
import com.ch.zishan.service.LearnedCardGroupService;
import com.ch.zishan.service.LearnedCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/learnedCardGroup")
public class LearnedCardGroupController {
    @Resource
    private LearnedCardGroupService learnedCardGroupService;

    @Resource
    private LearnedCardService learnedCardService;

    @GetMapping("/learnedCardList")
    public Result<List<LearnedCard>> getTodayLearnedCardList(Long learnedCardGroupId) {
        LearnedCardGroup learnedGroup = learnedCardGroupService.getById(learnedCardGroupId);
        if (learnedGroup == null) {
            return Result.error("卡片集不存在");
        }
        List<LearnedCard> learnedCardList
                = learnedCardService.getLearnedCardList(learnedCardGroupId, learnedGroup.getLearnedNum());
        if (learnedCardList == null || learnedCardList.isEmpty()) {
            return Result.success("201", "该卡片集已学习完毕");
        }
        return Result.success(learnedCardList);
    }
}
