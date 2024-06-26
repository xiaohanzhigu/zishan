package com.ch.zishan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.dto.CardGroupDto;
import com.ch.zishan.pojo.Card;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.pojo.Collect;
import com.ch.zishan.service.*;
import com.ch.zishan.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Transactional
@RequestMapping("/cardGroup")
public class CardGroupController {
    @Resource
    private CardGroupService cardGroupService;
    @Resource
    private CardService cardService;
    @Resource
    private ChapterService chapterService;
    @Resource
    private CollectService collectService;
    @Resource
    private LearnedCardGroupService learnedCardGroupService;

    @GetMapping("/collect/{id}")
    public Result<CardGroup> getCardGroupById(@PathVariable Long id) {
        log.info("查询卡片集，id：" + id);
        if (id == null) {
            return Result.error("id不能为空");
        }
        CardGroup group = cardGroupService.getCardGroupById(id);
        if (group == null) {
            return Result.error("卡片集不存在");
        }
        if (group.getIsDeleted() == 1 || group.getIsPublic() == 0) {
            return Result.error("卡片集不存在");
        }

        return Result.success(group);
    }

    @GetMapping("/search/{key}")
    public Result<List<CardGroup>> search(@PathVariable String key) {
        log.info("搜索卡片集，关键字：" + key);
        if (StringUtils.isBlank(key)) {
            return Result.error("关键字不能为空");
        }
        List<CardGroup> list = cardGroupService.search(key);
        return Result.success(list);
    }

    @PutMapping("/open")
    public Result<String> isPublic(@RequestBody CardGroup cardGroup) {
        log.info("设置卡片集是否公开，id：" + cardGroup.getId());
        // 只有创建卡片集者才可以设置
        QueryWrapper<CardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("id", cardGroup.getId());
        CardGroup group = cardGroupService.getOne(wrapper);

        if (group == null) {
            return Result.error("卡片集不存在");
        }

        if (!SysUtils.checkUser(BaseContext.get(),group.getCreateUser())) {
            return Result.error( "无权限设置");
        }

        UpdateWrapper<CardGroup> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", cardGroup.getId())
                .set("is_public", cardGroup.getIsPublic());
        cardGroupService.update(updateWrapper);
        log.info("设置卡片集是否公开成功，id：" + cardGroup.getId());
        return Result.success("设置成功");
    }

    @PutMapping("/recover")
    public Result<String> recoverCardGroup(@RequestBody CardGroup cardGroup) {
        log.info("恢复卡片集，id：" + cardGroup.getId());

        // 只有创建卡片集者才可以恢复
        QueryWrapper<CardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("id", cardGroup.getId()).eq("is_deleted", 1);
        CardGroup group = cardGroupService.getOne(wrapper);
        if (!SysUtils.checkUser(BaseContext.get(),group.getCreateUser())) {
            return Result.error("无权限恢复");
        }

        cardGroupService.recoverCardGroup(cardGroup.getId());
        return Result.success("恢复成功");
    }

    @DeleteMapping
    public Result<String> deleteCardGroup(@RequestParam Long id) {
        log.info("删除卡片集，id：" + id);
        // 只有创建卡片集者才可以删除
        QueryWrapper<CardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        CardGroup cardGroup = cardGroupService.getOne(wrapper);

        if (cardGroup == null) {
            return Result.error("卡片集不存在");
        }

        if (!SysUtils.checkUser(BaseContext.get(),cardGroup.getCreateUser())) {
            return Result.error("无权限删除");
        }
        cardGroupService.deleteCardGroup(id);
        log.info("删除卡片集成功，id：" + id);
        return Result.success("删除成功");
    }

    @DeleteMapping("/all")
    public Result<String> allDeleteCardGroup(@RequestParam Long id) {
        log.info("永久删除卡片集，id：" + id);
        // 只有创建卡片集者才可以删除
        QueryWrapper<CardGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        CardGroup cardGroup = cardGroupService.getOne(wrapper);

        if (cardGroup == null) {
            return Result.error("卡片集不存在");
        }

        if (!SysUtils.checkUser(BaseContext.get(),cardGroup.getCreateUser())) {
            return Result.error( "无权限删除");
        }
        cardGroupService.allDeleteCardGroup(id);
        log.info("永久删除卡片集成功，id：" + id);
        return Result.success("删除成功");
    }

    @PutMapping
    // 更改卡片集名称
    public Result<String> updateCardGroup(@RequestBody CardGroup cardGroup) {
        log.info("更新卡片集，id：" + cardGroup.getId());

        // 只有创建卡片集者才可以修改
        QueryWrapper<CardGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", cardGroup.getId());
        CardGroup group = cardGroupService.getOne(queryWrapper);

        if (group == null) {
            return Result.error("卡片集不存在");
        }

        if (!SysUtils.checkUser(BaseContext.get(),group.getCreateUser())) {
            return Result.error("无权限恢复");
        }

        UpdateWrapper<CardGroup> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", cardGroup.getId())
                        .set("name", cardGroup.getName());

        cardGroupService.update(wrapper);
        log.info("更新卡片集成功，id：" + cardGroup.getId());
        return Result.success("更新成功");
    }

    @PostMapping
    public Result<Long> addCardGroup() {
        log.info("添加卡片集");
        CardGroup cardGroup = new CardGroup();
        cardGroup.setName("无标题卡片集");
        cardGroup.setUser(BaseContext.get());
        cardGroupService.addCardGroup(cardGroup);
        log.info("添加卡片集成功，id：" + cardGroup.getId());
        return Result.success(cardGroup.getId());
    }

    @GetMapping("/{id}")
    public Result<CardGroupDto> detailDto(@PathVariable Long id) {
        QueryWrapper<CardGroup> groupWrapper = new QueryWrapper<>();
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        QueryWrapper<Card> cardWrapper = new QueryWrapper<>();

        groupWrapper.eq("id",id);
        CardGroup group = cardGroupService.getOne(groupWrapper);

        if (group == null) {
            return Result.error("卡片集不存在");
        }

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

        String msg;
        if (group.getCreateUser().equals(BaseContext.get())) {
            msg = "0";
        } else {
            msg = "1"; // 不是创建者
        }

        CardGroupDto groupDto = learnedCardGroupService.getReviewNumAndNotLearnedNum(BaseContext.get(), group.getId());
        if (groupDto != null) {
            BeanUtils.copyProperties(group, groupDto);
        }

        return Result.success(groupDto,msg);
    }


    @GetMapping("/all/{type}")
    public Result<List<CardGroupDto>> allCardGroupDto(@PathVariable String type) {
        long id = BaseContext.get();
        log.info("查询卡片集类型：" + type);

        QueryWrapper<CardGroup> cardGroupWrapper = new QueryWrapper<>();
        QueryWrapper<Chapter> chapterWrapper = new QueryWrapper<>();
        if ("我的卡片集".equals(type)) {
            cardGroupWrapper.eq("user", id)
                    .eq("is_deleted", 0);
        } else if("回收站".equals(type)) {
            cardGroupWrapper.eq("user", id)
                    .eq("is_deleted", 1);
        } else if("我的收藏".equals(type)) {
            QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<Collect>()
                    .eq("user_id", BaseContext.get())
                    .eq("is_deleted", 0);
            List<Collect> collectList = collectService.list(collectQueryWrapper);
            List<Long> cardGroupIds = new ArrayList<>();
            for (Collect collect : collectList) {
                cardGroupIds.add(collect.getCardGroupId());
            }
            if (!cardGroupIds.isEmpty()) {
                cardGroupWrapper.in("id", cardGroupIds).eq("is_deleted", 0);
            } else {
                return Result.success(new ArrayList<>());
            }

        } else {
            return Result.error("类型错误");
        }
        cardGroupWrapper.orderByDesc("update_time");
        List<CardGroupDto> dtoList = new ArrayList<>();
        List<CardGroup> list = cardGroupService.list(cardGroupWrapper);
        list.forEach(cardGroup -> {
            CardGroupDto reviewNumAndNotLearnedNum = learnedCardGroupService.getReviewNumAndNotLearnedNum(id, cardGroup.getId());
            if (reviewNumAndNotLearnedNum != null) {
                BeanUtils.copyProperties(cardGroup, reviewNumAndNotLearnedNum);
            }
            dtoList.add(reviewNumAndNotLearnedNum);
        });

        return Result.success(dtoList);
    }

}
