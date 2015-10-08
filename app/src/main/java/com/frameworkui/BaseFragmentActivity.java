package com.frameworkui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ThoLH on 10/7/15.
 */
public class BaseFragmentActivity extends AppCompatActivity {

    private FragmentManagerLayout mFragmentManager;
    private boolean isCreating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreating = true;
        getWindow().setBackgroundDrawable(null);
        mFragmentManager = new FragmentManagerLayout(this);
        mFragmentManager.init(null);
        setContentView(mFragmentManager);
    }

    public FragmentManagerLayout getFragmentManagerLayout() {
        return mFragmentManager;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager != null && mFragmentManager.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCreating) {
            if (getFragmentManagerLayout() != null) {
                getFragmentManagerLayout().onResume();
            }
        }
        isCreating = false;
    }

    @Override
    protected void onPause() {
        if (getFragmentManagerLayout() != null) {
            getFragmentManagerLayout().onPause();
        }
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getFragmentManagerLayout() != null) {
            getFragmentManagerLayout().onLowMemory();
        }
    }
}
