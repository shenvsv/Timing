package com.smilehacker.timing.model;

import com.smilehacker.timing.util.DLog;

import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.ModelList;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by kleist on 14-5-24.
 */
@Table("apps_category")
public class AppsCategory extends Model {
    @AutoIncrementPrimaryKey
    @Column("id")
    public long id;

    @Column("package_name")
    public String packageName;

    @Column("category_id")
    public int categoryId;

    public static List<AppsCategory> getByCategory(int id) {
        CursorList<AppsCategory> appsCategories = Query.many(AppsCategory.class,
                "SELECT * FROM apps_category WHERE category_id = ?",
                id)
                .get();
        return appsCategories.asList();
    }

    public static void linkPackageWithCategory(String packageName, int categoryId) {
        DLog.i("link " + packageName + " with " + categoryId);
        AppsCategory appsCategory = Query.one(AppsCategory.class,
                "SELECT * FROM apps_category WHERE package_name = ? AND category_id = ?",
                packageName, categoryId)
                .get();
        if (appsCategory == null) {
            DLog.i("add link");
            appsCategory = new AppsCategory();
            appsCategory.packageName = packageName;
            appsCategory.categoryId = categoryId;
            appsCategory.save();
        }
    }

    public static void unlinkPackageWithCategory(String packageName, int categoryId) {
        DLog.i("unlink " + packageName + " with " + categoryId);
        AppsCategory appsCategory = Query.one(AppsCategory.class,
                "SELECT * FROM apps_category WHERE package_name = ? AND category_id = ?",
                packageName, categoryId)
                .get();
        if (appsCategory != null) {
            DLog.i("remove link");
            appsCategory.delete();
        }

    }

    public static void deleteByCategory(long categoryId) {
        CursorList<AppsCategory> appsCategories = Query.many(AppsCategory.class,
                "SELECT * FROM apps_category WHERE category_id = ?",
                categoryId).get();
        ModelList.from(appsCategories).deleteAll();
    }
}
