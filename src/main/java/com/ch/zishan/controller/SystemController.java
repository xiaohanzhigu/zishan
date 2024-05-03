package com.ch.zishan.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.interceptor.AuthAccess;
import com.ch.zishan.pojo.CardGroup;
import com.ch.zishan.pojo.Collect;
import com.ch.zishan.pojo.Share;
import com.ch.zishan.pojo.User;
import com.ch.zishan.service.*;
import com.ch.zishan.utils.SysUtils;
import com.ch.zishan.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin
@RestController
public class SystemController {
    @Resource
    private UserService userService;
    @Resource
    private CollectService collectService;
    @Resource
    private ShareService shareService;
    @Resource
    private CardGroupService cardGroupService;

    @AuthAccess
    @GetMapping("/shareList/{code}")
    public Result<CardGroup> shareList(@PathVariable String code) {
        if (StrUtil.isBlank(code)) {
            return Result.error("404","分享码错误");
        }
        Share share = shareService
                .getOne(new QueryWrapper<Share>().eq("share_code", code));
        if (share == null) {
            return Result.error("404","分享码不存在");
        }
        CardGroup group = cardGroupService.getById(share.getCardGroupId());
        if (group == null) {
            return Result.error("404","卡片集不存在");
        }
        if (group.getIsPublic() == 0 ||group.getIsDeleted() == 1) {
            return Result.error("404","该分享码已失效");
        }
        group = cardGroupService.getCardGroupById(group.getId());
        return Result.success(group);
    }

    @PostMapping("/share")
    public Result<String> share(@RequestBody Share share) {
        CardGroup group = cardGroupService.getById(share.getCardGroupId());
        if (group == null) {
            return Result.error("404","卡片集不存在");
        }
        if (group.getIsPublic() == 0) {
            return Result.error("404","卡片集不公开");
        }
        if (group.getIsDeleted() == 1) {
            return Result.error("404","卡片集不存在");
        }
        shareService.remove(new QueryWrapper<Share>().eq("card_group_id", share.getCardGroupId()));
        share.setUserId(BaseContext.get());
        share.setShareCode(RandomStringUtils.randomAlphanumeric(10));
        shareService.save(share);
        return Result.success(share.getShareCode());
    }

    @DeleteMapping("/collect")
    public Result<String> cancelCollect(@RequestParam Long cardGroupId) {
        if (cardGroupId == null) {
            return Result.error("404","收藏不存在");
        }
        collectService.deleteCollect(BaseContext.get(), cardGroupId);
        return Result.success("取消收藏成功");

    }

    @PostMapping("/collect/{shareCode}")
    public Result<String> collect(@PathVariable String shareCode) {
        if (StrUtil.isBlank(shareCode)) {
            return Result.error("404","分享码错误");
        }
        Share share = shareService
                .getOne(new QueryWrapper<Share>().eq("share_code", shareCode));
        if (share == null) {
            return Result.error("404","分享码不存在");
        }
        CardGroup group = cardGroupService.getById(share.getCardGroupId());
        if (group == null) {
            return Result.error("404","卡片集不存在");
        }
        if (group.getIsPublic() == 0 ||group.getIsDeleted() == 1) {
            return Result.error("404","该分享码已失效");
        }
        if (SysUtils.checkUser(group.getCreateUser(), BaseContext.get()) ||
                collectService.getOne(new QueryWrapper<Collect>()
                        .eq("user_id", BaseContext.get())
                        .eq("card_group_id", share.getCardGroupId())) != null){
            return Result.error("404","已经收藏过了");
        }

        Collect collect = new Collect();
        collect.setUserId(BaseContext.get());
        collect.setCardGroupId(share.getCardGroupId());
        collect.setCollectDate(TimeUtils.getCurrentTimeStamp());
        collectService.addCollect(collect);
        return Result.success("收藏成功");
    }

    @GetMapping("/logout")
    public Result<String> logout() {
        BaseContext.remove();
        return Result.success("退出成功");
    }

    @AuthAccess
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        log.info("用户注册:" + user.toString());
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }

        if (userService.selectByUsername(user.getUsername()) != null) {
            return Result.error("404","用户名已存在");
        }
        user.setNickname(user.getUsername());
        user = userService.register(user);

        if (user == null) {
            return Result.error("404","注册失败");
        }
        return Result.success(user);
    }

    @AuthAccess
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        log.info("用户登录:" + user.getUsername());
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())) {
            return Result.error("数据输入不合法");
        }
        user = userService.login(user);
        BaseContext.set(user.getId());
        return Result.success(user);
    }

}
