package com.libowen.diaocha.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void println(Object o){
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime now = LocalDateTime.now(zoneId);
        Thread thread=Thread.currentThread();
        String message = String.format("%s [%d - %s]: %s\n", formatter.format(now), thread.getId(), thread.getName(), o.toString());

        System.out.println(message);
    }
}
