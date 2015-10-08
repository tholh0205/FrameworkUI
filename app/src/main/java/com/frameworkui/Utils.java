package com.frameworkui;

import android.graphics.Paint;
import android.os.Build;
import android.view.View;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class Utils {

    public static int sStatusBarHeight = 0;

    public static final void setLayerType(View view, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= 11) {
            view.setLayerType(layerType, paint);
        }
    }

}
