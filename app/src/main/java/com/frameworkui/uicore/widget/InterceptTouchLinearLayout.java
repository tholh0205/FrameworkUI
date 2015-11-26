package com.frameworkui.uicore.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.frameworkui.uicore.Utils;

/**
 * Created by ThoLH on 11/2/15.
 */
public class InterceptTouchLinearLayout extends LinearLayout {
    public InterceptTouchLinearLayout(Context context) {
        super(context);
    }

    public InterceptTouchLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InterceptTouchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private boolean mEatingTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (Utils.USE_SLIDE_TO_BACK) {
            final int action = MotionEventCompat.getActionMasked(ev);
            if (action == MotionEvent.ACTION_DOWN) {
                mEatingTouch = false;
            }

            if (!mEatingTouch) {
                final boolean handled = super.onTouchEvent(ev);
                if (action == MotionEvent.ACTION_DOWN && !handled) {
                    mEatingTouch = true;
                }
            }

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mEatingTouch = false;
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }
}
