package com.smilehacker.timing.model;

import android.provider.ContactsContract;

import com.smilehacker.timing.app.Constants;
import com.smilehacker.timing.util.DLog;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
@Table("app_record")
public class AppRecord extends Model {

    @AutoIncrementPrimaryKey
    @Column("id")
    public long id;

    @Column("package_name")
    public String packageName;

    @Column("date")
    public String date;

    @Column("duration")
    public int duration;

    public static void increaseData(String packageName) {
        String today = formatDate(new Date());
        AppRecord appRecord = Query.one(AppRecord.class,
                "SELECT * FROM app_record WHERE package_name = ? AND date = ?",
                packageName, today).get();
        if (appRecord != null) {
            appRecord.duration += Constants.TRICK_INTERVAL_SECOND;
            appRecord.save();
        } else {
            appRecord = new AppRecord();
            appRecord.packageName = packageName;
            appRecord.date = today;
            appRecord.duration = 1;
            appRecord.save();
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static Date formatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static List<AppRecord> getRecordByDate(Date begin, Date end) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record WHERE date >= ? AND date <= ? ORDER BY duration DESC",
                formatDate(begin), formatDate(end))
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }

    public static List<AppRecord> getRecordByDate(Date date) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record WHERE date = ? ORDER BY duration DESC",
                formatDate(date))
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }
}
