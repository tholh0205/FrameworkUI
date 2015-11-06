package com.frameworkui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by ThoLH on 11/6/15.
 */
public class HackyViewPager extends ViewPager {

    public HackyViewPager(Context context) {
        super(context);
        setHackyScroller();
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHackyScroller();
    }

    private void setHackyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new HackyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class HackyScroller extends Scroller {

        public HackyScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, duration / 2);
        }
    }

}
