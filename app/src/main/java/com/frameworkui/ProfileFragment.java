package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ProfileFragment extends BaseFragment {
    @Override
    public View onCreateView(Context context, ViewGroup container) {
        mFragmentView = LayoutInflater.from(context).inflate(R.layout.profile_fragment, container, false);
        return mFragmentView;
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if (actionBar != null) {
            setTitle("Profile Fragment");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        Intent data = new Intent();
//        data.putExtra("ThoLH", "Testing");
//        setResult(Activity.RESULT_OK, data);
//        return super.onBackPressed();
//    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("ThoLH", "ProfileFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("ThoLH", "ProfileFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("ThoLH", "ProfileFragment onDestroy");
    }

}
