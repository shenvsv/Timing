package com.smilehacker.timing.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smilehacker.timing.R;
import com.smilehacker.timing.adapter.AppRecordListAdapter;
import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.model.DailyRecord;
import com.smilehacker.timing.model.event.CategoryEvent;
import com.smilehacker.timing.model.event.DataChangeEvent;
import com.smilehacker.timing.util.AppRecordHelper;
import com.smilehacker.timing.view.GraphView;
import com.smilehacker.timing.view.MyListView;

import java.util.ArrayList;
import java.util.Calendar;
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
    private LinearLayout mGraphLayer;
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
        mGraphLayer = (LinearLayout) view.findViewById(R.id.graph_layer);
        mLvAppRecord.setAdapter(mListAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
        mGraphView.show();
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

    private List<DailyRecord> loadDailyRecord(Calendar calendar) {
        return DailyRecord.getDurationByDate(calendar);
    }

    public void onEvent(CategoryEvent event) {
        load(event.id);
        if (event.id == CategoryEvent.ID_ALL){
            mGraphView.show();
        } else {
            mGraphView.show(event.id);
        }

    }


    public void onEvent(DataChangeEvent event) {
        if (event.action == DataChangeEvent.ACTION_CATEGORY_DELETED) {
            load();
            mGraphView.show();
        }
    }
}
