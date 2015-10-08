package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;

import java.util.HashMap;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class BaseFragment {

    private boolean isFinished = false;
    public View mFragmentView = null;
    private Toolbar mToolbar = null;
    protected Bundle mArguments = null;
    private BaseFragmentActivity mBaseActivity = null;

    //Fragment Result Data
    public int mResultCode = Activity.RESULT_CANCELED;
    public Intent mData = null;

    public BaseFragment() {
    }

    public BaseFragment(Bundle arguments) {
        this.mArguments = arguments;
    }

    public BaseFragment(BaseFragmentActivity baseActivity, Bundle arguments) {
        this.mBaseActivity = baseActivity;
        this.mArguments = arguments;
    }

    public void setActivity(BaseFragmentActivity baseActivity) {
        this.mBaseActivity = baseActivity;
    }

    public BaseFragmentActivity getActivity() {
        return mBaseActivity;
    }

    public void setArguments(Bundle arguments) {
        this.mArguments = arguments;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public Toolbar getToolbar() {
        if (mToolbar == null && mFragmentView != null)
            mToolbar = (Toolbar) mFragmentView.findViewById(R.id.toolbar);
        return mToolbar;
    }

    public void onSetupActionBar() {
        getToolbar();
        if (mToolbar != null && mBaseActivity != null) {
            mBaseActivity.setSupportActionBar(mToolbar);
            ActionBar actionBar = mBaseActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(getUpIndicator());
            }
        }
    }

    protected Drawable getUpIndicator() {
        Drawable up = getActivity().getResources().getDrawable(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        up.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return up;
    }

    public boolean onOptionItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return finishFragment(false);
        }
        return false;
    }

    public void setTitle(int res) {
        if (getToolbar() != null)
            getToolbar().setTitle(res);
    }

    public void setTitle(CharSequence title) {
        if (getToolbar() != null)
            getToolbar().setTitle(title);
    }

    public void setSubtitle(int res) {
        if (getToolbar() != null)
            getToolbar().setSubtitle(res);
    }

    public void setSubtitle(CharSequence subtitle) {
        if (getToolbar() != null)
            getToolbar().setSubtitle(subtitle);
    }

    public View onCreateView(Context context, ViewGroup container) {
        return null;
    }

    public void onViewCreated(View view) {
        this.mFragmentView = view;
    }

    protected void clearViews() {
        if (mFragmentView != null) {
            ViewParent parent = mFragmentView.getParent();
            if (parent != null && ViewGroup.class.isInstance(parent)) {
                try {
                    ((ViewGroup) parent).removeView(mFragmentView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mFragmentView = null;
        }
    }

    public boolean finishFragment(boolean animated) {
        if (mBaseActivity != null && mBaseActivity.getFragmentManagerLayout() != null) {
            return mBaseActivity.getFragmentManagerLayout().goToBackStack(animated);
        }
        return false;
    }

    public boolean onBackPressed() {
//        return finishFragment(false);
        return false;
    }

    protected void setResult(int resultCode, Intent data) {
        this.mResultCode = resultCode;
        this.mData = data;
        if (mData == null) {
            mData = new Intent();
            mData.putExtras(mArguments);
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
        clearViews();
    }

    public void onOpenAnimationStart() {
    }

    public void onOpenAnimationEnd() {
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            return;
        if (mData != null)
            outState.putBundle("mResultData", mData.getExtras());
        if (mArguments != null)
            outState.putBundle("mArguments", mArguments);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            return;
        Bundle data = savedInstanceState.containsKey("mResultData") ? savedInstanceState.getBundle("mResultData") : null;
        if (data != null) {
            mData = new Intent();
            mData.putExtras(data);
        }
        mArguments = savedInstanceState.containsKey("mArguments") ? savedInstanceState.getBundle("mArguments") : null;
    }

    public interface SingleInstance {
    }
}
