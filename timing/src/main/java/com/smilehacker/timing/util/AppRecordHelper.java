package com.smilehacker.timing.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.model.AppRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
public class AppRecordHelper {

    private PackageManager mPackageManager;

    public AppRecordHelper(Context context) {
        mPackageManager = context.getPackageManager();
    }

    public List<AppInfo> loadAppsByDate(Calendar date) {
        List<AppRecord> appRecords = AppRecord.getRecordByDate(date);
        List<AppInfo> appInfos = new ArrayList<AppInfo>(appRecords.size());

        for (AppRecord appRecord: appRecords) {
            AppInfo appInfo = new AppInfo();
            PackageInfo packageInfo = getPkgInfoByPkgName(appRecord.packageName);

            if (packageInfo == null) {
                continue;
            }

            appInfo.appName = mPackageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
            appInfo.packageName = appRecord.packageName;
            appInfo.setTime(appRecord.duration);
            appInfos.add(appInfo);
        }

        return appInfos;
    }

    public List<AppInfo> loadAppsByDateAndCategory(Calendar date, long categoryId) {
        List<AppRecord> appRecords = AppRecord.getRecordByDateAndCategory(date, categoryId);
        List<AppInfo> appInfos = new ArrayList<AppInfo>(appRecords.size());

        for (AppRecord appRecord: appRecords) {
            AppInfo appInfo = new AppInfo();
            PackageInfo packageInfo = getPkgInfoByPkgName(appRecord.packageName);

            if (packageInfo == null) {
                continue;
            }

            appInfo.appName = mPackageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
            appInfo.packageName = appRecord.packageName;
            appInfo.setTime(appRecord.duration);
            appInfos.add(appInfo);
        }

        return appInfos;
    }

    public List<AppInfo> loadAppsByDateWithUnsigned(Calendar date) {
        List<AppRecord> appRecords = AppRecord.getRecordByDateWithUnsigned(date);
        List<AppInfo> appInfos = new ArrayList<AppInfo>(appRecords.size());

        for (AppRecord appRecord: appRecords) {
            AppInfo appInfo = new AppInfo();
            PackageInfo packageInfo = getPkgInfoByPkgName(appRecord.packageName);

            if (packageInfo == null) {
                continue;
            }

            appInfo.appName = mPackageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
            appInfo.packageName = appRecord.packageName;
            appInfo.setTime(appRecord.duration);
            appInfos.add(appInfo);
        }

        return appInfos;
    }
    private PackageInfo getPkgInfoByPkgName(String pkgName) {
        PackageInfo info;
        try {
            info = mPackageManager.getPackageInfo(pkgName, 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            DLog.e(String.format("package %1$s not found", pkgName));
            return null;
        }
    }
}
