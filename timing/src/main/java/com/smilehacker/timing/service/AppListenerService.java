package com.smilehacker.timing.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import com.smilehacker.timing.model.AppRecord;
import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.util.AppListener;
import com.smilehacker.timing.util.DLog;

import java.util.Date;

public class AppListenerService extends Service {


    public final static String KEY_COMMAND = "key_command";
    public final static String COMMAND_TRICK_APP = "command_trick_app";
    public final static String COMMAND_START_TRICK = "command_start_trick";

    private AppListener mAppListener;
    private AlarmManager mAlarmManager;
    private PendingIntent mTrickPendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppListener = new AppListener(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra(KEY_COMMAND);
        if (COMMAND_START_TRICK.equals(command)) {
            startListener();
        } else if (COMMAND_TRICK_APP.equals(command)) {
            trickForegroundApp();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListener();
    }

    private void stopListener() {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mTrickPendingIntent);
            mAlarmManager = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void startListener() {
        Intent intent = new Intent(this, AppListenerService.class);
        intent.putExtra(KEY_COMMAND, COMMAND_TRICK_APP);
        mTrickPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggertTime = SystemClock.elapsedRealtime();
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggertTime, 1000, mTrickPendingIntent);
    }

    private void trickForegroundApp() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String packageName = mAppListener.getForegroundPackage();
                AppRecord.increaseData(packageName);
                DailyRecord.addRecordByNow();
                return null;
            }
        }.execute();
    }


}
