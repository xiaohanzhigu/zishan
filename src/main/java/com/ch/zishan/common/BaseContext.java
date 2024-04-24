package com.ch.zishan.common;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long get() {
        return threadLocal.get();
    }

    public static void set(Long id) {
        threadLocal.set(id);
    }
}
