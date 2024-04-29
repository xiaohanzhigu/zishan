package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Collect;

public interface CollectService extends IService<Collect> {

    public void addCollect(Collect collect);
}
