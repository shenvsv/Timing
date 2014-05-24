package com.smilehacker.timing.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.smilehacker.timing.R;
import com.smilehacker.timing.model.Record;
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
//        onStart();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        onStart();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        onStart();
    }
    public void show(){
        final RecordHelper helper = new RecordHelper();
        final Resources resources = getResources();
        new AsyncTask<Void, Void, List<Record>>(){

            @Override
            protected List<Record> doInBackground(Void... voids) {
                return helper.getRecordByDate(Calendar.getInstance());
            }
            @Override
            protected void onPostExecute(List<Record> infos) {
                super.onPostExecute(infos);
                DLog.i("//"+infos.size());
                for (int i = 0; i < infos.size(); i++ ){
                    Record info = infos.get(i);
                    PieSlice slice = new PieSlice();
                    slice.setColor(resources.getColor(R.color.green_light));
                    slice.setSelectedColor(resources.getColor(R.color.transparent_blue));
                    slice.setValue(info.second);
                    GraphView.this.addSlice(slice);
                }
                GraphView.this.setInnerCircleRatio(150);
                GraphView.this.setPadding(2);
            }
        }.execute();
    }
}
