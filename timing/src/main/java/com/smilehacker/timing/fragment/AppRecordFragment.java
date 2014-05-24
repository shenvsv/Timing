package com.smilehacker.timing.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.adapter.AppRecordListAdapter;
import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.model.Category;
import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.model.event.CategoryEvent;
import com.smilehacker.timing.util.AppRecordHelper;
import com.smilehacker.timing.util.RecordHelper;
import com.smilehacker.timing.view.GraphView;
import com.smilehacker.timing.view.MyListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by kleist on 14-5-24.
 */
public class AppRecordFragment extends Fragment {

    private MyListView mLvAppRecord;
    private AppRecordHelper mAppRecordHelper;
    private AppRecordListAdapter mListAdapter;
    private GraphView mGraphView;
    private EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppRecordHelper = new AppRecordHelper(getActivity());
        mListAdapter = new AppRecordListAdapter(getActivity(), new ArrayList<AppInfo>());
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_app_record, container, false);
        mLvAppRecord = (MyListView) view.findViewById(R.id.lv_app_record);
        mGraphView = (GraphView) view.findViewById(R.id.graph_pie);
        mLvAppRecord.setAdapter(mListAdapter);
        initListView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
        showGraph();
    }

    private void initListView() {
//        View view = new View(getActivity());
//        ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.graph_view_height));
//        view.setLayoutParams(lp);
//        view.setEnabled(false);
//        mLvAppRecord.addHeaderView(view);
    }

    private class NoTouchView extends View {

        public NoTouchView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }



    }

    private void load() {
        load(CategoryEvent.ID_ALL);
    }

    private void load(final long id) {
        new AsyncTask<Void, Void, List<AppInfo>>() {

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                List<AppInfo> appInfos;
                if (id == CategoryEvent.ID_ALL) {
                    appInfos = mAppRecordHelper.loadAppsByDate(Calendar.getInstance());
                } else if (id == CategoryEvent.ID_UNSIGNED){
                    appInfos = mAppRecordHelper.loadAppsByDateWithUnsigned(Calendar.getInstance());
                } else {
                    appInfos = mAppRecordHelper.loadAppsByDateAndCategory(Calendar.getInstance(), id);
                }
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

    public void onEvent(CategoryEvent event) {
        load(event.id);
    }
}
