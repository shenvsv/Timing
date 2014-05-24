package com.smilehacker.timing.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.adapter.AppRecordListAdapter;
import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.util.AppRecordHelper;
import com.smilehacker.timing.view.GraphView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
public class AppRecordFragment extends Fragment {

    private ListView mLvAppRecord;
    private AppRecordHelper mAppRecordHelper;
    private AppRecordListAdapter mListAdapter;
    private GraphView mGraphView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppRecordHelper = new AppRecordHelper(getActivity());
        mListAdapter = new AppRecordListAdapter(getActivity(), new ArrayList<AppInfo>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_app_record, container, false);
        mLvAppRecord = (ListView) view.findViewById(R.id.lv_app_record);
        mGraphView = (GraphView) view.findViewById(R.id.graph_pie);
        mLvAppRecord.setAdapter(mListAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
        showGraph();
    }

    private void load() {
        new AsyncTask<Void, Void, List<AppInfo>>() {

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                List<AppInfo> appInfos = mAppRecordHelper.loadAppsByDate(new Date());
                return appInfos;
            }

            @Override
            protected void onPostExecute(List<AppInfo> appInfos) {
                super.onPostExecute(appInfos);
                mListAdapter.flush(appInfos);
            }
        }.execute();
    }

    private void showGraph(){
        new AsyncTask<Void, Void, List<DailyRecord>>(){

            @Override
            protected List<DailyRecord> doInBackground(Void... voids) {
                return loadDailyRecord(Calendar.getInstance());
            }

            @Override
            protected void onPostExecute(List<DailyRecord> dailyRecords) {
                super.onPostExecute(dailyRecords);
                mGraphView.show(dailyRecords);
            }
        }.execute();
    }

    private List<DailyRecord> loadDailyRecord(Calendar calendar) {
        return DailyRecord.getDurationByDate(calendar);
    }
}
