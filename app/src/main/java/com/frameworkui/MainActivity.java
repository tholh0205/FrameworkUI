package com.frameworkui;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            presentFragment(FragmentType.MAIN, null, -1, NO_ANIMATION);
        }
    }
}
