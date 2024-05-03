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
        return recordMapper.selectOne(wrapper);
    }

//    public Record getRecordByUserIdAndData(Long userId, Long )
}
