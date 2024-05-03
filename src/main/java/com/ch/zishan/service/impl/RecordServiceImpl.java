package com.ch.zishan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.zishan.mapper.RecordMapper;
import com.ch.zishan.pojo.Record;
import com.ch.zishan.service.RecordService;
import com.ch.zishan.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    public Record getTodayRecordByUserId(Long userId) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .between("time", TimeUtils.getTodayStartStamp(), TimeUtils.getTodayEndStamp());
        Record record = recordMapper.selectOne(wrapper);
        if (record == null) {
            record = new Record();
            record.setUserId(userId);
            record.setTime(TimeUtils.getCurrentDateStamp());
            recordMapper.insert(record);
        }
        return record;
    }

    @Override
    public Integer getTodayLearnedNumByUserId(Long userId) {
        Record record = getTodayRecordByUserId(userId);
        return record.getLearnedNum();
    }

    @Override
    public void dayPlanNumAddOne(Long userId) {
        Record record = getTodayRecordByUserId(userId);
            record.setLearnedNum(record.getLearnedNum() + 1);
            recordMapper.updateById(record);
    }

    @Override
    public void editNumAddOne(Long userId) {
        Record record = getTodayRecordByUserId(userId);
        record.setEditNum(record.getEditNum() + 1);
        recordMapper.updateById(record);
    }

    @Override
    public void addNumAddOne(Long userId) {
        Record record = getTodayRecordByUserId(userId);
        record.setAddNum(record.getAddNum() + 1);
        recordMapper.updateById(record);
    }

}
