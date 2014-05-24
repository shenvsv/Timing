package com.smilehacker.timing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.util.AsyncIconLoader;

import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
public class AppRecordListAdapter extends BaseAdapter {

    private List<AppInfo> mAppInfos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private AsyncIconLoader mAsyncIconLoader;

    public AppRecordListAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        mAppInfos = appInfos;
        mLayoutInflater = LayoutInflater.from(context);
        mAsyncIconLoader = new AsyncIconLoader(context);
    }

    public void flush(List<AppInfo> appInfos) {
        mAppInfos.clear();
        mAppInfos.addAll(appInfos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mAppInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_app_record, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.duration = (TextView) view.findViewById(R.id.tv_duration);
            holder.icon = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AppInfo appInfo = mAppInfos.get(i);
        holder.name.setText(appInfo.appName);
        holder.duration.setText(appInfo.time);
        mAsyncIconLoader.loadBitmap(appInfo.packageName, holder.icon);

        return view;
    }

    private static class ViewHolder {
        public TextView duration;
        public TextView name;
        public ImageView icon;
    }

    private void getColorByDuration(long duration) {

    }
}
