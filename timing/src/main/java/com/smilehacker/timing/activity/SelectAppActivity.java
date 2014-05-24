package com.smilehacker.timing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.smilehacker.timing.R;
import com.smilehacker.timing.adapter.AppGridViewAdapter;
import com.smilehacker.timing.model.PackageModel;
import com.smilehacker.timing.util.AppManager;

import java.util.ArrayList;
import java.util.List;

public class SelectAppActivity extends Activity {

    public final static String KEY_CATEGORY_ID = "key_category_id";

    private GridView mGvApps;
    private ProgressBar mPbLoading;
    private int mCategoryId;

    private AppGridViewAdapter mAppGridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
        Intent intent = getIntent();
        mCategoryId = intent.getIntExtra(KEY_CATEGORY_ID, 0);
        initView();
        showApps();
    }


    private void initView() {
        mGvApps = (GridView) findViewById(R.id.gv_apps);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        mAppGridViewAdapter = new AppGridViewAdapter(this, new ArrayList<PackageModel>(), mCategoryId);
        mGvApps.setAdapter(mAppGridViewAdapter);
    }


    private void showApps() {
        AsyncTask<Void, Void, List<PackageModel>> loadAppTask = new AsyncTask<Void, Void, List<PackageModel>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mGvApps.setVisibility(View.GONE);
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<PackageModel> doInBackground(Void... voids) {
                AppManager appManager = new AppManager(SelectAppActivity.this);
                return appManager.loadApps(mCategoryId);
            }

            @Override
            protected void onPostExecute(List<PackageModel> packageModels) {
                super.onPostExecute(packageModels);
                mAppGridViewAdapter.flush(packageModels);
                mPbLoading.setVisibility(View.GONE);
                mGvApps.setVisibility(View.VISIBLE);
            }
        };

        loadAppTask.execute();
    }
}
