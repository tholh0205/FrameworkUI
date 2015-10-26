package com.frameworkui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/7/15.
 */
public class BaseFragmentActivity extends AppCompatActivity {

    private final String SAVED_FRAGMENT_STACK = "SAVED_FRAGMENT_STACK";

    private FragmentManagerLayout mFragmentManager;
    private boolean isCreating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreating = true;
        getWindow().setBackgroundDrawable(null);
        mFragmentManager = new FragmentManagerLayout(this);
        setContentView(mFragmentManager);
        if (savedInstanceState != null) {
            mFragmentManager.setSavedInstanceState(savedInstanceState);
            if (savedInstanceState.containsKey(SAVED_FRAGMENT_STACK)) {
                ArrayList<FragmentData.FragmentItem> data = savedInstanceState.getParcelableArrayList(SAVED_FRAGMENT_STACK);
                mFragmentManager.init(data);
                mFragmentManager.showLastFragment();
                mFragmentManager.recreateAllFragmentViews(false);
            }
        } else {
            mFragmentManager.init(null);
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_FRAGMENT_STACK, getFragmentManagerLayout().getFragmentStack());
        getFragmentManagerLayout().onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getFragmentManagerLayout() != null) {
            getFragmentManagerLayout().onCreateOptionsMenu(menu, getMenuInflater());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getFragmentManagerLayout() != null) {
            getFragmentManagerLayout().onOptionItemSelected(item);
        }
        return true;
    }
}
