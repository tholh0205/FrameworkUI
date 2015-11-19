package com.frameworkui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class Utils {

    public static final boolean IS_DEBUGGING = MainApplication.getInstance().getAppContext().getResources().getBoolean(R.bool.isDebug);

    public static final boolean USE_SLIDE_TO_BACK = Build.VERSION.SDK_INT >= 11;

    public static int sStatusBarHeight = 0;

    public static boolean isTablet() {
        return false;
    }

    public static final void setLayerType(View view, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= 11) {
            view.setLayerType(layerType, paint);
        }
    }

    public static float getPixelsInCM(float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? MainApplication.getInstance().getResources().getDisplayMetrics().xdpi : MainApplication.getInstance().getResources().getDisplayMetrics().ydpi);
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static int dp(float dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) Math.ceil(MainApplication.getInstance().getResources().getDisplayMetrics().density * dp);
    }

    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                Field mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                mStableInsetsField.setAccessible(true);
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
