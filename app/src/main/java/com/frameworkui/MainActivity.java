package com.frameworkui;

import android.os.Bundle;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManagerLayout().showFragment(FragmentData.FragmentType.MAIN, null, 0, false, false);
        }
    }
}
