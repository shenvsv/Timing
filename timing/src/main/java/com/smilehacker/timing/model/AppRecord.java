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
import java.util.Calendar;
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
        String today = formatDate(Calendar.getInstance());
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

    public static String formatDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    public static Date formatDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static List<AppRecord> getRecordByDate(Calendar begin, Calendar end) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record WHERE date >= ? AND date <= ? ORDER BY duration DESC",
                formatDate(begin), formatDate(end))
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }

    public static List<AppRecord> getRecordByDate(Calendar date) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record WHERE date = ? ORDER BY duration DESC",
                formatDate(date))
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }

    public static List<AppRecord> getRecordByDateAndCategory(Calendar date, long categoryId) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record, apps_category WHERE app_record.date = ? AND app_record.package_name = apps_category.package_name AND apps_category.category_id = ? ORDER BY duration DESC",
                formatDate(date), categoryId)
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }

    public static List<AppRecord> getRecordByDateWithUnsigned(Calendar date) {
        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record WHERE date = ? AND package_name NOT IN (SELECT package_name FROM apps_category) ORDER BY duration DESC",
                formatDate(date))
                .get();
        List<AppRecord> list = appRecords.asList();
        appRecords.close();
        return list;
    }
}
