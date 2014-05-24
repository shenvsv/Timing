package com.smilehacker.timing.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.smilehacker.timing.R;
import com.smilehacker.timing.model.DailyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenvsv on 14-5-24.
 */
public class GraphView extends BarGraph {


    public GraphView(Context context) {
        super(context);
        onStart();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onStart();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onStart();
    }

    private void onStart(){
        final Resources resources = getResources();
        ArrayList<Bar> aBars = new ArrayList<Bar>();
        for (int i = 0; i < 24 ; i++){
            Bar bar = new Bar();
            bar.setColor(resources.getColor(R.color.green_light));
            bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
            bar.setName("Test1");
            bar.setValue(1000);
            bar.setValueString("1,000");
            aBars.add(bar);
        }

        this.setBars(aBars);
        this.setAxisColor(R.color.transparent_blue);
        this.setShowBarText(true);
        this.setShowAxis(true);

        this.setOnBarClickedListener(new OnBarClickedListener() {

            @Override
            public void onClick(int index) {

            }
        });

    }
    public void show(List<DailyRecord> dailyRecords){
//        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.getLayoutParams();
//        linearParams.weight = this.getHeight()/3;
//        this.setLayoutParams(linearParams);
       // onStart();
    }
}
