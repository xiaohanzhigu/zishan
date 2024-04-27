package com.ch.zishan.utils;

public class SysUtils {
    public static boolean checkUser(Long userId,Long createUserId) {
        return userId.equals(createUserId) ;
    }
}
