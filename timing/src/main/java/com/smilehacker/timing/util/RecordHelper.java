package com.smilehacker.timing.util;


import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

}
