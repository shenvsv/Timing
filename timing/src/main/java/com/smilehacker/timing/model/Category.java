package com.smilehacker.timing.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.annotations.AutoIncrementPrimaryKey;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by kleist on 14-5-24.
 */
@Table("category")
public class Category extends Model {

    @AutoIncrementPrimaryKey
    @Column("id")
    public long id;

    @Column("name")
    public String name;

    public static List<Category> getAllCategories() {
        CursorList<Category> categories = Query.all(Category.class).get();
        return categories.asList();
    }

    public static Category getCategoryById(long id) {
        Category category = Query.one(Category.class, "SELECT * FROM category WHERE id = ?", id).get();
        return category;
    }

    public static void deleteCategoryById(long id) {
        Category category = getCategoryById(id);
        category.delete();
    }
}
