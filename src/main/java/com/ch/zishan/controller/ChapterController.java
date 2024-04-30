package com.ch.zishan.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.ChapterService;
import com.ch.zishan.utils.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Resource
    private ChapterService chapterService;

    @DeleteMapping
    public Result<String> deletedChapter(@RequestParam Long id) {
        log.info("删除章节，id：" + id);
        Chapter chapter = chapterService.getById(id);
        if (chapter == null) {
            return Result.error("章节不存在");
        }
        if (!SysUtils.checkUser(BaseContext.get(),chapter.getCreateUser())) {
            return Result.error("无权限删除");
        }
        chapterService.deleteChapter(id);
        return Result.success("删除成功");
    }

    @PutMapping
    public Result<Boolean> updateChapterName(@RequestBody Chapter chapter) {
        UpdateWrapper<Chapter> wrapper = new UpdateWrapper<>();

        Chapter chapterById = chapterService.getById(chapter.getId());
        if (chapterById == null) {
            return Result.error("章节不存在");
        }
        if (!SysUtils.checkUser(BaseContext.get(),chapterById.getCreateUser())) {
            return Result.error("无权限删除");
        }

        wrapper.eq("id", chapter.getId())
                .set("name", chapter.getName());
        chapterService.update(wrapper);
        log.info("更新章节成功，id：" + chapter.getId());
        return Result.success(true);
    }

    @PostMapping
    public Result<Boolean> addChapter(@RequestBody Chapter chapter) {
        chapter.setName("新建章节");
        chapterService.addChapter(chapter);
        log.info("添加章节成功，id：" + chapter.getId());
        return Result.success(true);
    }
}
