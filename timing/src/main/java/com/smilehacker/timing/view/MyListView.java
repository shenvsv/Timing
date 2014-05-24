package com.smilehacker.timing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.util.DLog;

/**
 * Created by kleist on 14-5-25.
 */
public class MyListView extends ListView {

    private View mHeader;
    private int mHeaderHeight;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.graph_view_height);
        addHeader();
    }

    private void addHeader() {
        mHeader = new View(getContext());
        ListView.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeaderHeight);
        mHeader.setLayoutParams(lp);
        this.addHeaderView(mHeader);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isTouchHeader(ev)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isTouchHeader(ev)) {
            return false;
        }
        return super.onTouchEvent(ev);
    }


    private Boolean isTouchHeader(MotionEvent event) {
        int y = (int) event.getY();
        return y < mHeaderHeight + mHeader.getY();
    }

}
