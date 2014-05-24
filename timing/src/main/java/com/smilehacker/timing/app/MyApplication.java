package com.smilehacker.timing.app;

import android.app.Application;

import com.smilehacker.timing.model.AppRecord;
import com.smilehacker.timing.model.AppsCategory;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.model.DailyRecord;

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
        migration.createTable(Category.class);
        migration.createTable(AppsCategory.class);
        migration.createTable(DailyRecord.class);
        sprinkles.addMigration(migration);
    }
}
