package com.frameworkui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class BaseFragment {

    boolean isFinished = false;
    boolean isResumed = false;
    boolean isRemoving = false;
    boolean isPaused = false;
    boolean isAdded = false;
    public View mFragmentView = null;
    private Toolbar mToolbar = null;
    protected Bundle mArguments = null;
    private BaseFragmentActivity mBaseActivity = null;
    BaseFragment mParentFragment;
    private ChildFragmentManager mChildFragmentManager = new ChildFragmentManager();

    public ChildFragmentManager getChildFragmentManager() {
        return mChildFragmentManager;
    }

    public BaseFragment getParentFragment() {
        return mParentFragment;
    }

    //Fragment Result Data
    public int mResultCode = Activity.RESULT_CANCELED;
    public Intent mData = null;

    boolean hasMenu = false;

    protected void setHasMenu(boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    public BaseFragment() {
        mChildFragmentManager.mParentFragment = this;
    }

    public BaseFragment(Bundle arguments) {
        this.mArguments = arguments;
        mChildFragmentManager.mParentFragment = this;
    }

    public BaseFragment(BaseFragmentActivity baseActivity, Bundle arguments) {
        this.mBaseActivity = baseActivity;
        this.mArguments = arguments;
        mChildFragmentManager.mParentFragment = this;
    }

    public void setActivity(BaseFragmentActivity baseActivity) {
        this.mBaseActivity = baseActivity;
        mChildFragmentManager.mActivity = baseActivity;
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

    protected Drawable getMenuMoreDrawable() {
        Drawable up = getActivity().getResources().getDrawable(android.support.v7.appcompat.R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        up.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return up;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    public boolean onOptionItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return finishFragment(true);
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mFragmentView = view;
    }

    public View getView() {
        return mFragmentView;
    }


    public void onCreate(Bundle savedInstanceState) {
        isFinished = false;
        isResumed = false;
        mChildFragmentManager.mSavedInstanceState = savedInstanceState;
    }

    public void onNewIntent() {
    }

    public void onAttach(Activity activity) {
        isAdded = true;
        isPaused = false;
        isResumed = false;
    }

    public void onDetach() {
        isAdded = false;
        isPaused = false;
        isResumed = false;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
    }

    protected void clearViews() {
        if (mFragmentView != null) {
            ViewParent parent = mFragmentView.getParent();
            if (parent != null && ViewGroup.class.isInstance(parent)) {
                try {
                    ((ViewGroup) parent).removeView(mFragmentView);
                    onDetach();
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
        return finishFragment(true);
//        return false;
    }

    protected void setResult(int resultCode, Intent data) {
        this.mResultCode = resultCode;
        this.mData = data;
        if (mData == null) {
            mData = new Intent();
            mData.putExtras(mArguments);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onResume() {
        isResumed = true;
        isPaused = false;
        mChildFragmentManager.onResume();
    }

    public void onPause() {
        isResumed = false;
        isPaused = true;
        mChildFragmentManager.onPause();
    }

    public void onDestroy() {
        mChildFragmentManager.onDestroy();
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
        mChildFragmentManager.onSaveInstanceState(outState);
    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState == null)
//            return;
//        Bundle data = savedInstanceState.containsKey("mResultData") ? savedInstanceState.getBundle("mResultData") : null;
//        if (data != null) {
//            mData = new Intent();
//            mData.putExtras(data);
//        }
//        mArguments = savedInstanceState.containsKey("mArguments") ? savedInstanceState.getBundle("mArguments") : null;
//    }

    public boolean isFinished() {
        return isFinished;
    }

    public void onLowMemory() {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mChildFragmentManager != null) {
            mChildFragmentManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean isEnableSwipeBack() {
        return true;
    }

    public interface SingleInstance {
    }

    public interface KeepBelowFragment {
    }

    public interface ReusableFragment extends SingleInstance {
    }

}
