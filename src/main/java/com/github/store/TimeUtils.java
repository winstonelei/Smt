package com.github.store;

public class TimeUtils {

    private TimeUtils() {
    }

    public static void sleepInMills(long mills) {
        if (mills <= 0) {
            return;
        }
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            //
        }

    }

    public static void sleepOneSecond(long s, long e) {
        long cost = 1000 + s - e;
        TimeUtils.sleepInMills(cost);
    }
}
