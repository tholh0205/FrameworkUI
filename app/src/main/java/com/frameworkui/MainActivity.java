package com.frameworkui;

import android.os.Bundle;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            MainApplication.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (getFragmentManagerLayout() != null)
                        getFragmentManagerLayout().showFragment(FragmentData.FragmentType.MAIN, null, 0, false, false);
                }
            });
        }
    }
}
