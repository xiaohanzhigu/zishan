package com.ch.zishan.utils;

import java.util.HashMap;
import java.util.Map;

public class SysUtils {
    public static boolean checkUser(Long userId,Long createUserId) {
        return userId.equals(createUserId) ;
    }

    private static Map<Integer,Integer> days = new HashMap<>();

    static {
        days.put(1,1);
        days.put(2,2);
        days.put(3,4);
        days.put(4,7);
        days.put(5,15);
        days.put(6,21);
    }
    public static Integer getDegree(Integer level) {
        return days.get(level);
    }
}
