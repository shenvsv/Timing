package com.smilehacker.timing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.model.AppsCategory;
import com.smilehacker.timing.model.PackageModel;
import com.smilehacker.timing.util.AsyncIconLoader;

import java.util.List;


/**
 * Created by kleist on 14-5-19.
 */
public class AppGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<PackageModel> mPackageModels;
    private LayoutInflater mLayoutInflater;
    private AsyncIconLoader mAsyncIconLoader;
    private int mCategoryId;

    public AppGridViewAdapter(Context context, List<PackageModel> packageModels, int categoryId) {
        mContext = context;
        mPackageModels = packageModels;
        mLayoutInflater = LayoutInflater.from(context);
        mAsyncIconLoader = new AsyncIconLoader(context);
        mCategoryId = categoryId;
    }

    @Override
    public int getCount() {
        return mPackageModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mPackageModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_gv_app, viewGroup, false);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.cbEnable = (CheckBox) view.findViewById(R.id.cb_enable);
            holder.rlCard = (RelativeLayout) view.findViewById(R.id.rl_card);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final PackageModel packageModel = mPackageModels.get(i);
        holder.tvName.setText(packageModel.name);
        holder.cbEnable.setChecked(packageModel.checked);
        mAsyncIconLoader.loadBitmap(packageModel.packageName, holder.ivIcon);

        holder.rlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                packageModel.checked = !packageModel.checked;
                holder.cbEnable.setChecked(packageModel.checked);

                if (packageModel.checked) {
                    AppsCategory.linkPackageWithCategory(packageModel.packageName, mCategoryId);
                } else {
                    AppsCategory.unlinkPackageWithCategory(packageModel.packageName, mCategoryId);
                }
            }
        });

        return view;
    }

    public void flush(List<PackageModel> packageModels) {
        mPackageModels.clear();
        mPackageModels.addAll(packageModels);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public CheckBox cbEnable;
        public RelativeLayout rlCard;
    }


}
