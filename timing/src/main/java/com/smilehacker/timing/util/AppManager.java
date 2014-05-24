package com.smilehacker.timing.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.smilehacker.timing.model.AppsCategory;
import com.smilehacker.timing.model.PackageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 14-5-19.
 */
public class AppManager {

    private Context mContext;
    private PackageManager mPackageManager;

    public AppManager(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    public List<PackageModel> loadApps(int categoryId) {
        List<PackageModel> packageModels = loadAppsFromSys();
        List<AppsCategory> appsCategories = loadAppsFromDB(categoryId);
        DLog.i("from db = " + appsCategories.size());

        for (AppsCategory appsCategory : appsCategories) {
            for (PackageModel packageModel : packageModels) {
                if (packageModel.packageName.equals(appsCategory.packageName)) {
                    DLog.i("category " + categoryId + " get " + packageModel.packageName);
                    packageModel.checked = true;
                    break;
                }
            }
        }

        sortListByEnableState(packageModels);
        return packageModels;
    }

    private List<PackageModel> loadAppsFromSys() {
        List<PackageModel> list = new ArrayList<PackageModel>();
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(PackageManager.PERMISSION_GRANTED);

        for (PackageInfo pkg : packages) {
            if (!isLaunchable(pkg)) {
                continue;
            }
            PackageModel packageModel = new PackageModel();
            packageModel.name = mPackageManager.getApplicationLabel(pkg.applicationInfo).toString();
            packageModel.packageName = pkg.packageName;
            list.add(packageModel);
        }

        return list;
    }

    private List<AppsCategory> loadAppsFromDB(int categoryId) {
        return AppsCategory.getByCategory(categoryId);
    }

    private void sortListByEnableState(List<PackageModel> packageModels) {
        int pos = 0;
        PackageModel tmpModel = packageModels.get(pos);
        for (int i = 0, size = packageModels.size(); i < size; i++) {
            PackageModel packageModel = packageModels.get(i);
            if (packageModel.checked) {
                packageModels.set(pos, packageModel);
                packageModels.set(i, tmpModel);
                pos++;
                tmpModel = packageModels.get(pos);
            }
        }
    }


    private Boolean isLaunchable(PackageInfo packageInfo) {
        return  mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null;
    }
}
