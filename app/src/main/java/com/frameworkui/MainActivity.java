package com.frameworkui;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presentFragment(new MainFragment(), false, NO_ANIMATION);
    }
}
