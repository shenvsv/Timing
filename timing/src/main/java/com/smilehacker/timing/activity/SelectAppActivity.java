package com.smilehacker.timing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.smilehacker.timing.model.AppsCategory;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.model.PackageModel;
import com.smilehacker.timing.model.event.DataChangeEvent;
import com.smilehacker.timing.util.AppManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SelectAppActivity extends Activity {

    public final static String KEY_CATEGORY_ID = "key_category_id";

    private GridView mGvApps;
    private ProgressBar mPbLoading;
    private int mCategoryId;

    private AppGridViewAdapter mAppGridViewAdapter;
    private EventBus mEventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
        mEventBus = EventBus.getDefault();
        Intent intent = getIntent();
        mCategoryId = intent.getIntExtra(KEY_CATEGORY_ID, 0);
        initView();
        showApps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_delete) {
            deleteCategory();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void deleteCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Really to delete this category?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Category.deleteCategoryById(mCategoryId);
                        AppsCategory.deleteByCategory(mCategoryId);
                        SelectAppActivity.this.finish();
                        DataChangeEvent event = new DataChangeEvent();
                        event.action = DataChangeEvent.ACTION_CATEGORY_DELETED;
                        mEventBus.post(event);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}
