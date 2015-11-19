package com.frameworkui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.frameworkui.Utils;

/**
 * Created by ThoLH on 11/19/15.
 */
public class ResizeFrameLayout extends FrameLayout {

    private Rect rect = new Rect();

    public ResizeFrameLayout(Context context) {
        super(context);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        android.util.Log.d("ThoLH", "ResizeFrameLayout height " + MeasureSpec.getSize(heightMeasureSpec) + " keyboardHeight == " + getKeyboardHeight());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        android.util.Log.d("ThoLH", "ResizeFrameLayout onLayout " + " keyboardHeight == " + getKeyboardHeight());
    }

    public int getKeyboardHeight() {
        View rootView = getRootView();
        getWindowVisibleDisplayFrame(rect);
        int usableViewHeight = rootView.getHeight() - (rect.top != 0 ? Utils.sStatusBarHeight : 0) - Utils.getViewInset(rootView);
        return usableViewHeight - (rect.bottom - rect.top);
    }
}
