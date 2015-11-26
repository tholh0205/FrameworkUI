package com.frameworkui;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.frameworkui.uicore.Utils;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class MainApplication extends Application {

    private static MainApplication mInstance = null;

    public static MainApplication getInstance() {
        return mInstance;
    }

    public Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    private Handler mHandlerUI;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mHandlerUI = new Handler(Looper.getMainLooper());
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Utils.sStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }

    public void runOnUIThread(Runnable runnable) {
        mHandlerUI.post(runnable);
    }
}
