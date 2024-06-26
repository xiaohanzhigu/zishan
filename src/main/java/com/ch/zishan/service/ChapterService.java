package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Chapter;

public interface ChapterService extends IService<Chapter> {

    public boolean addChapter(Chapter chapter);

    public Integer deleteOrRecoverChapterLogic(Long cardGroupID, Integer isDeleted);

    public Integer deleteChapter(Long id);
}
