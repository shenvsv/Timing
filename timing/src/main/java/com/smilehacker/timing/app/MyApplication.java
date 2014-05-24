package com.smilehacker.timing.app;

import android.app.Application;

import com.smilehacker.timing.model.AppRecord;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by kleist on 14-5-24.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB() {
        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());
        Migration migration = new Migration();
        migration.createTable(AppRecord.class);
        sprinkles.addMigration(migration);
    }
}
