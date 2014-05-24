package com.smilehacker.timing.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by kleist on 14-5-24.
 */
@Table("daily_record")
public class DailyRecord extends Model {

    @AutoIncrementPrimaryKey
    @Column("id")
    public long id;

    @Column("date")
    public String date;

    @Column("hour")
    public int hour;

    @Column("duration")
    public int duration;

    public static void addRecordByNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(calendar.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        DailyRecord dailyRecord = Query.one(DailyRecord.class,
                "SELECT * FROM daily_record WHERE date = ? AND hour = ?",
                date, hour).get();
        if (dailyRecord != null) {
            dailyRecord.duration += 1;
            dailyRecord.save();
        } else {
            dailyRecord = new DailyRecord();
            dailyRecord.date = date;
            dailyRecord.hour = hour;
            dailyRecord.duration = 1;
            dailyRecord.save();
        }
    }

    public static List<DailyRecord> getDurationByDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(calendar.getTime());
        CursorList<DailyRecord> dailyRecords = Query.many(DailyRecord.class,
                "SELECT * FROM daily_record WHERE date = ?",
                date).get();
        return dailyRecords.asList();
    }
}
