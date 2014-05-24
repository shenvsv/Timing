package com.smilehacker.timing.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.activity.SelectAppActivity;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.util.DLog;

import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
public class CategoryListAdapter extends BaseAdapter {

    private List<Category> mCategories;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public CategoryListAdapter(Context context, List<Category> categories) {
        mCategories = categories;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void flush(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int i) {
        return mCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_category, viewGroup, false);
            holder = new ViewHolder();
            holder.categoryName = (TextView) view.findViewById(R.id.tv_category);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Category category = mCategories.get(i);
        holder.categoryName.setText(category.name);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(mContext, SelectAppActivity.class);
                DLog.i("long click id = " + category.id);
                intent.putExtra(SelectAppActivity.KEY_CATEGORY_ID, category.id);
                mContext.startActivity(intent);
                return true;
            }
        });

        return view;
    }

    private static class ViewHolder {
        public TextView categoryName;
    }
}
