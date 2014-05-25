package com.smilehacker.timing.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.smilehacker.timing.R;
import com.smilehacker.timing.model.AppInfo;
import com.smilehacker.timing.model.Record;
import com.smilehacker.timing.model.event.CategoryEvent;
import com.smilehacker.timing.util.AppRecordHelper;
import com.smilehacker.timing.util.DLog;
import com.smilehacker.timing.util.RecordHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by shenvsv on 14-5-24.
 */
public class GraphView extends PieGraph {


    public GraphView(Context context) {
        super(context);

    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public void show(){
        final RecordHelper helper = new RecordHelper();
        final Resources resources = getResources();
        final long[] totaltime = {0};
        new AsyncTask<Void, Void, List<Record>>(){

            @Override
            protected List<Record> doInBackground(Void... voids) {
                return helper.getRecordByDate(Calendar.getInstance());
            }
            @Override
            protected void onPostExecute(List<Record> infos) {
                super.onPostExecute(infos);
                GraphView.this.removeSlices();

                for (int i = 0; i < infos.size(); i++ ){
                    Record info = infos.get(i);
                    PieSlice slice = new PieSlice();

                    if (info.title.equals("games")){
                        slice.setColor(resources.getColor(R.color.light_red));
                        slice.setSelectedColor(resources.getColor(R.color.red));
                    }else
                    if (info.title.equals("work")){
                        slice.setColor(resources.getColor(R.color.light_blue));
                        slice.setSelectedColor(resources.getColor(R.color.blue));
                    }else
                    if (info.title.equals("社交")) {
                        slice.setColor(resources.getColor(R.color.light_orange));
                        slice.setSelectedColor(resources.getColor(R.color.orange));
                    }
                    else{
                        slice.setColor(resources.getColor(R.color.light_green));
                        slice.setSelectedColor(resources.getColor(R.color.green));
                    }
                    slice.setValue(info.second);
                    slice.setTitle(info.title);
                    //DLog.i(""+info.second+info.title);
                    totaltime[0] = info.second + totaltime[0];
                    GraphView.this.addSlice(slice);
                }

                DLog.i("t"+totaltime[0]);
                int mark = (int)(100*totaltime[0]/(24*60*60));
                GraphView.this.setText(""+mark+"%");
                GraphView.this.setInnerCircleRatio(150);
                GraphView.this.setPadding(2);
                GraphView.this.rate = ""+mark+"%";
                GraphView.this.setOnSliceClickedListener(new OnSliceClickedListener() {
                    @Override
                    public void onClick(int index) {

                    }
                });
            }
        }.execute();
    }
    public void show(final long id){
        final AppRecordHelper helper = new AppRecordHelper(getContext());
        final Resources resources = getResources();
        final long[] totaltime = {0};
        new AsyncTask<Void, Void, List<AppInfo>>(){

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                List<AppInfo> appInfos;
                if (id == CategoryEvent.ID_ALL) {
                    appInfos = helper.loadAppsByDate(Calendar.getInstance());
                } else if (id == CategoryEvent.ID_UNSIGNED){
                    appInfos = helper.loadAppsByDateWithUnsigned(Calendar.getInstance());
                } else {
                    appInfos = helper.loadAppsByDateAndCategory(Calendar.getInstance(), id);
                }
                return appInfos;
            }
            @Override
            protected void onPostExecute(List<AppInfo> infos) {
                super.onPostExecute(infos);
                GraphView.this.removeSlices();

                for (int i = 0; i < infos.size(); i++ ){
                    AppInfo info = infos.get(i);
                    PieSlice slice = new PieSlice();

                    if (i == 0){
                        slice.setColor(resources.getColor(R.color.light_red));
                        slice.setSelectedColor(resources.getColor(R.color.red));
                    }else
                    if (i == 1){
                        slice.setColor(resources.getColor(R.color.light_blue));
                        slice.setSelectedColor(resources.getColor(R.color.blue));
                    }else
                    if (i == 2) {
                        slice.setColor(resources.getColor(R.color.light_orange));
                        slice.setSelectedColor(resources.getColor(R.color.orange));
                    }
                    else{
                        slice.setColor(resources.getColor(R.color.light_green));
                        slice.setSelectedColor(resources.getColor(R.color.green));
                    }
                    slice.setValue(info.duration);
                    slice.setTitle(info.appName);
                    //DLog.i(""+info.second+info.title);
                    totaltime[0] = info.duration + totaltime[0];
                    GraphView.this.addSlice(slice);
                }

                DLog.i("t"+totaltime[0]);
                int mark = (int)(100*totaltime[0]/(24*60*60));
                GraphView.this.setText(""+mark+"%");
                GraphView.this.setInnerCircleRatio(150);
                GraphView.this.setPadding(2);
                GraphView.this.rate = ""+mark+"%";
                GraphView.this.setOnSliceClickedListener(new OnSliceClickedListener() {
                    @Override
                    public void onClick(int index) {

                    }
                });
            }
        }.execute();
    }



}
