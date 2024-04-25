package com.ch.zishan.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.CardGroupService;
import com.ch.zishan.service.CardService;
import com.ch.zishan.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/cardGroup")
public class CardGroupController {
    @Resource
    private CardGroupService cardGroupService;
    @Resource
    private CardService cardService;
    @Resource
    private ChapterService chapterService;

    @GetMapping("/allCardGroup")
    public Result<List<CardGroup>> allCardGroup(HttpServletRequest request, @RequestParam String type) {
        String token = request.getHeader("Token");
        Integer id = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        log.info("查询卡片集类型：" + type);

        QueryWrapper<CardGroup> cardGroupWrapper = new QueryWrapper<>();
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();
        if ("我的卡片集".equals(type)) {
            cardGroupWrapper.eq("user", id)
                    .eq("is_deleted", 0);
        }

        List<CardGroup> list = cardGroupService.list(cardGroupWrapper);

        for (CardGroup group : list) {
            chapterWrapper.eq("card_group",group.getId());
            List<Chapter> chapterList =  chapterService.list(chapterWrapper);
            group.setChapterList(chapterList);

            for (Chapter chapter : chapterList) {
                group.setCardTotal(group.getCardTotal() + chapter.getTotal());
            }
        }

        return Result.success(list);
    }
}
