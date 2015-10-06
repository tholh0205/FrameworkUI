package com.frameworkui;

import android.app.Application;
import android.content.Context;

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

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Utils.sStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
    }
}
