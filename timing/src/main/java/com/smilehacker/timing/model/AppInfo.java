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

        if (hour == 0) {
            if (mintue < 1) {
                mintue = 1;
            }
            return String.format("%1$dmin", mintue);
        } else {
            return String.format("%1$dh %2$2dmin", hour, mintue);
        }
    }
}
