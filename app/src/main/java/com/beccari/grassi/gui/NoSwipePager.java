package com.beccari.grassi.gui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Michele on 01/10/2017.
 */

public class NoSwipePager extends ViewPager {
    public NoSwipePager(Context context) {
        super(context);
    }

    public NoSwipePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
