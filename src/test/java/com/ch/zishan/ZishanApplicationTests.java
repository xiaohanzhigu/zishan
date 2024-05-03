package com.ch.zishan;

import com.ch.zishan.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;


class ZishanApplicationTests {

    @Test
    void contextLoads() throws ParseException {
        System.out.println(TimeUtils.getCurrentDateStamp());
        System.out.println(TimeUtils.getTomorrowDateStamp());
    }

}
