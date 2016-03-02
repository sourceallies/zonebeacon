package com.sourceallies.android.zonebeacon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class InterceptingLinearLayout extends LinearLayout {
    public InterceptingLinearLayout(Context context) {
        super(context);
    }

    public InterceptingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev){
        return true;
    }
}