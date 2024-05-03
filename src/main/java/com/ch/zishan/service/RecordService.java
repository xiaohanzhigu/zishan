package com.ch.zishan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.zishan.pojo.Record;

public interface RecordService extends IService<Record> {
    public Record getTodayRecordByUserId(Long userId);
}
