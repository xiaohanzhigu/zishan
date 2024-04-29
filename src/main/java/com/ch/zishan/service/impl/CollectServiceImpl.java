package com.ch.zishan.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.common.BaseContext;
import com.ch.zishan.mapper.CollectMapper;
import com.ch.zishan.pojo.Collect;
import com.ch.zishan.service.CollectService;
import com.ch.zishan.service.LearnedCardGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Resource
    private LearnedCardGroupService learnedCardGroupService;


    @Override
    @Transactional
    public void addCollect(Collect collect) {
        this.save(collect);
        // 创建学习计划
        learnedCardGroupService.addCardGroupToLearnedCardGroup(BaseContext.get(), collect.getCardGroupId());
    }
}
