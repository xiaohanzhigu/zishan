package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Record;

public interface RecordService extends IService<Record> {
    public Record getTodayRecordByUserId(Long userId);

    public Integer getTodayLearnedNumByUserId(Long userId);

    public void dayPlanNumAddOne(Long userId);

    public void editNumAddOne(Long userId);

    public void addNumAddOne(Long userId);
}
