package com.smilehacker.timing.util;


import android.widget.ListView;

import com.smilehacker.timing.model.AppRecord;
import com.smilehacker.timing.model.AppsCategory;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by kleist on 14-5-24.
 */
public class RecordHelper {

    public List<Record> getDailyRecordByDate(Calendar calendar) {
        List<Record> records = new ArrayList<Record>(24);
        List<DailyRecord> dailyRecords = DailyRecord.getDurationByDate(calendar);
        for (int i = 0; i < 24; i++) {
            Record record = new Record();
            for (DailyRecord dailyRecord : dailyRecords) {
                if (dailyRecord.hour == i) {
                    record.setTime(dailyRecord.duration);
                    break;
                }
            }
            if (record.title == null) {
                record.title = Integer.toString(i);
                record.setTime(0);
            }
        }
        return records;
    }

    public List<Record> getDailyRecordByWeek(Calendar start, Calendar end) {
        int duration = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        List<Record> records = new ArrayList<Record>(duration);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");

        for (int i = 0; i < duration; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, start.get(Calendar.DAY_OF_YEAR) + i);
            List<DailyRecord> dailyRecords = DailyRecord.getDurationByDate(calendar);
            int second = 0;
            for (DailyRecord dailyRecord : dailyRecords) {
                second += dailyRecord.duration;
            }
            Record record = new Record();
            record.setTime(second);
            record.title = format.format(calendar.getTime());
            records.add(record);
        }

        return records;
    }

    public List<Record> getRecordByDate(Calendar calendar) {
        List<Record> records = new ArrayList<Record>();
        List<Category> categories = Category.getAllCategories();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (Category category : categories) {
            CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                    "SELECT * FROM app_record, apps_category WHERE app_record.package_name = apps_category.package_name AND apps_category.category_id = ? AND app_record.date = ?",
                    category.id, format.format(calendar.getTime())).get();
            long duration = 0;
            for (AppRecord appRecord : appRecords) {
                duration += appRecord.duration;
            }
            appRecords.close();

            Record record = new Record();
            record.title = category.name;
            record.setTime(duration);
            records.add(record);
        }

        return records;
    }

    public List<Record> getRecordByDate(Calendar start, Calendar end) {
        List<Record> records = new ArrayList<Record>();
        List<Category> categories = Category.getAllCategories();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (Category category : categories) {
            CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                    "SELECT * FROM app_record, apps_category WHERE app_record.package_name = apps_category.package_name AND apps_category.category_id = ? AND app_record.date >= ? AND app_record.date <= ?",
                    category.id, format.format(start.getTime()), format.format(end.getTime())).get();
            long duration = 0;
            for (AppRecord appRecord : appRecords) {
                duration += appRecord.duration;
            }
            appRecords.close();

            Record record = new Record();
            record.title = category.name;
            record.setTime(duration);
            records.add(record);
        }

        return records;
    }

    public Record getRecordByDateAndCategory(Calendar calendar, long categoryId) {
    Category category = Category.getCategoryById(categoryId);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        CursorList<AppRecord> appRecords = Query.many(AppRecord.class,
                "SELECT * FROM app_record, apps_category WHERE app_record.package_name = apps_category.package_name AND apps_category.category_id = ? AND app_record.date = ?",
                categoryId, format.format(calendar.getTime())).get();
        long duration = 0;
        for (AppRecord appRecord : appRecords) {
            duration += appRecord.duration;
        }
        appRecords.close();

        Record record = new Record();
        record.title = category.name;
        record.setTime(duration);

        return record;
    }

}
