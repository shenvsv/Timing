package com.smilehacker.timing.util;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by shenvsv on 14-5-25.
 */
public class DateHelper {



    public void getDate(Context context ,DatePickerDialog.OnDateSetListener listener){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(context,listener,year,month,day).show();
    }
}
