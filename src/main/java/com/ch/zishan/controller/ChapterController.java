package com.ch.zishan.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ch.zishan.common.Result;
import com.ch.zishan.pojo.Chapter;
import com.ch.zishan.service.ChapterService;
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

    @PutMapping
    public Result<Boolean> updateChapterName(@RequestBody Chapter chapter) {
        UpdateWrapper<Chapter> wrapper = new UpdateWrapper<>();
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
