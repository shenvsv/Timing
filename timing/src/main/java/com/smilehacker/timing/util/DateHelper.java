package com.smilehacker.timing.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by shenvsv on 14-5-25.
 */
public class DateHelper {
    private static Calendar mStartDate;
    private static Calendar mEndDate;

    public static void getDate(final Context context){
        mStartDate = Calendar.getInstance();
        final int year = mStartDate.get(Calendar.YEAR);
        final int month = mStartDate.get(Calendar.MONTH);
        final int day = mStartDate.get(Calendar.DAY_OF_MONTH);
        mEndDate = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                mEndDate .set(Calendar. YEAR , i);
                mEndDate .set(Calendar. MONTH , i2);
                mEndDate .set(Calendar. DAY_OF_MONTH , i3);
            }
        };
        DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                DLog.i("//"+i+"/"+i2+"/"+i3);
                mStartDate .set(Calendar.YEAR, i);
                mStartDate .set(Calendar. MONTH , i2);
                mStartDate .set(Calendar. DAY_OF_MONTH , i3);
                new DatePickerDialog(context,listener2,year,month,day).show();
            }
        };
        new DatePickerDialog(context,listener1,year,month,day).show();
    }

}
