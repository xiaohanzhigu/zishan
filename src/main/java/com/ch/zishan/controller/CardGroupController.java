package com.ch.zishan.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ch.zishan.common.BaseContext;
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
import java.util.ArrayList;
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

    @PutMapping
    public Result<Boolean> updateCardGroup(@RequestBody CardGroup cardGroup) {
        UpdateWrapper<CardGroup> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", cardGroup.getId())
                        .set("name", cardGroup.getName());

        cardGroupService.update(wrapper);
        log.info("更新卡片集成功，id：" + cardGroup.getId());
        return Result.success(true);
    }

    @PostMapping("/addCardGroup")
    public Result<Long> addCardGroup() {
        CardGroup cardGroup = new CardGroup();
        cardGroup.setName("无标题卡片集");
        cardGroup.setUser(BaseContext.get());
        cardGroupService.addCardGroup(cardGroup);
        log.info("添加卡片集成功，id：" + cardGroup.getId());
        return Result.success(cardGroup.getId());
    }

    @GetMapping("/detail")
    public Result<CardGroup> detail(@RequestParam Integer id) {
        QueryWrapper<CardGroup> groupWrapper = new QueryWrapper<>();
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();

        groupWrapper.eq("id",id);
        CardGroup group = cardGroupService.getOne(groupWrapper);
        chapterWrapper.eq("card_group",group.getId());
        List<Chapter> chapters = chapterService.list(chapterWrapper);
        List<Chapter> chapterList = new ArrayList<>();
        for (Chapter chapter : chapters) {
            //查询章节信息
            chapterWrapper.clear();
            chapterWrapper.eq("id",chapter.getId());
            Chapter one = chapterService.getOne(chapterWrapper);

            //查询章节中的卡片
            cardWrapper.clear();
            cardWrapper.eq("chapter",chapter.getId());
            List<Card> cards = cardService.list(cardWrapper);
            one.setCardList(cards);
            chapterList.add(one);
        }
        group.setChapterList(chapterList);
        return Result.success(group);
    }

    @GetMapping("/allCardGroup")
    public Result<List<CardGroup>> allCardGroup(HttpServletRequest request, @RequestParam String type) {
        String token = request.getHeader("Token");
        Long id = Long.valueOf(JWT.decode(token).getAudience().get(0));
        log.info("查询卡片集用户：" + id);
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
//            for (Chapter chapter : chapterList) {
//                group.setCardTotal(group.getCardTotal() + chapter.getTotal());
//            }
        }

        return Result.success(list);
    }
}
