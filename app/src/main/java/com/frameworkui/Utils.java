package com.frameworkui;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class Utils {

    public static final boolean USE_SLIDE_TO_BACK = Build.VERSION.SDK_INT >= 11;

    public static int sStatusBarHeight = 0;

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

    public static int dp(float dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) Math.ceil(MainApplication.getInstance().getResources().getDisplayMetrics().density * dp);
    }

}
