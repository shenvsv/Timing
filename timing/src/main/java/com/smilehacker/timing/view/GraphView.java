package com.smilehacker.timing.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.smilehacker.timing.R;
import com.smilehacker.timing.model.DailyRecord;

import java.util.List;

/**
 * Created by shenvsv on 14-5-24.
 */
public class GraphView extends PieGraph {


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
        PieSlice slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.green_light));
        slice.setSelectedColor(resources.getColor(R.color.transparent_orange));
        slice.setValue(2);
        this.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.orange));
        slice.setValue(3);
        this.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(resources.getColor(R.color.purple));
        slice.setValue(8);
        this.addSlice(slice);
        this.setInnerCircleRatio(150);
        this.setPadding(2);
    }
    public void show(List<DailyRecord> dailyRecords){
//        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.getLayoutParams();
//        linearParams.weight = this.getHeight()/3;
//        this.setLayoutParams(linearParams);
//       onStart();
    }
}
