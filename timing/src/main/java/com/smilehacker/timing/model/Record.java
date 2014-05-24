package com.smilehacker.timing.model;

/**
 * Created by kleist on 14-5-24.
 */
public class Record {
    public String title;
    public long second;
    public String time;

    public void setTime(long second) {
        this.second = second;
        this.time = formatTime(second);
    }

    private String formatTime(long second) {
        long hour = second / 3600;
        second %= 3600;
        long mintue = second / 60;
        second %= 60;

        return String.format("%1$d:%2$02d:%3$02d", hour, mintue, second);
    }
}
