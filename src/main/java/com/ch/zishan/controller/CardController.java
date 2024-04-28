package com.ch.zishan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.ChapterService;
import com.ch.zishan.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/card")
public class CardController {

    @Resource
    private CardService cardService;

    @Resource
    private ChapterService chapterService;

    @GetMapping
    public Result<Card> getCard(@RequestParam Long id) {
        log.info("获取卡片，id：" + id);
        QueryWrapper<Card> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Card card = cardService.getOne(wrapper);
        if (card == null) {
            return Result.error("404","卡片不存在");
        }
        return Result.success(card);
    }

    @DeleteMapping
    public Result<String> deletedCard(@RequestParam Long id) {
        log.info("删除卡片，id：" + id);
        QueryWrapper<Card> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        Card card = cardService.getOne(wrapper);
        if (card == null) {
            return Result.error("404","卡片不存在");
        }

        if (!SysUtils.checkUser(BaseContext.get(), card.getCreateUser())) {
            return Result.error("401", "无权限删除");
        }
        cardService.removeById(id);
        return Result.success("删除成功");
    }

    @PostMapping
    public Result<String> addCard(@RequestBody Card card) {
        log.info("添加卡片" + card.toString());

        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("id", card.getChapter());
        Chapter chapter = chapterService.getOne(wrapper);

        if (chapter == null) {
            return Result.error("404","章节不存在");
        }
        log.info("用户" + BaseContext.get() + "添加卡片" + chapter.toString());
        if (!SysUtils.checkUser(BaseContext.get(), chapter.getCreateUser())) {
            return Result.error("添加失败，无权限");
        }

        cardService.addCard(card);
        return Result.success("添加成功");
    }
}
