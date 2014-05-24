package com.smilehacker.timing.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kleist on 14-5-24.
 */
public class AppInfo {

    public String packageName;

    public String appName;

    public long duration;

    public String time;

    public void setTime(long time) {
        this.duration = time;
        this.time = formatTime(time);
    }

    private String formatTime(long second) {
        long hour = second / 3600;
        second %= 3600;
        long mintue = second / 60;
        second %= 60;

        return String.format("%1$d:%2$02d:%3$02d", hour, mintue, second);
    }
}
