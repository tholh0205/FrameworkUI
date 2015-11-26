package com.frameworkui.uicore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.frameworkui.R;
import com.frameworkui.uicore.actionbar.ActionBar;
import com.frameworkui.uicore.actionbar.ActionBarMenu;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class BaseFragment {

    boolean isFinished = false;
    boolean isResumed = false;
    boolean isRemoving = false;
    boolean isPaused = false;
    boolean isAdded = false;
    boolean isStopped = false;
    boolean isStarted = false;
    public View mFragmentView = null;
    //    private Toolbar mToolbar = null;
    protected Bundle mArguments = null;
    private BaseFragmentActivity mBaseActivity = null;
    BaseFragment mParentFragment;
    protected ActionBar mActionBar;
    protected ActionBarMenu mMenu;

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

    protected boolean hasMenu = false;

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

//    public Toolbar getToolbar() {
//        if (mToolbar == null && mFragmentView != null)
//            mToolbar = (Toolbar) mFragmentView.findViewById(R.id.toolbar);
//        return mToolbar;
//    }


    public void onSetupActionBar() {
//        getToolbar();
//        if (mToolbar != null && mBaseActivity != null) {
//            mBaseActivity.setSupportActionBar(mToolbar);
//            ActionBar actionBar = mBaseActivity.getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//                actionBar.setHomeAsUpIndicator(getUpIndicator());
//            }
//        }

        mActionBar = (com.frameworkui.uicore.actionbar.ActionBar) mFragmentView.findViewById(R.id.custom_action_bar);
        if (mActionBar != null) {
            mActionBar.setItemsBackground(R.drawable.bar_selector);
            mActionBar.parentFragment = this;
            mActionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                @Override
                public void onItemClick(int id) {
                    onOptionItemSelected(id);
                }
            });
            if (hasMenu) {
                if (mMenu == null) {
                    mMenu = mActionBar.createMenu();
                    onCreateOptionsMenu(mMenu);
                }
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

    public void onCreateOptionsMenu(ActionBarMenu menu) {
    }

    public boolean onOptionItemSelected(int menuId) {
        if (menuId == -1) {
            return finishFragment(true);
        }
        return false;
    }

    public void setTitle(CharSequence title) {
//        if (getToolbar() != null)
//            getToolbar().setTitle(title);
    }


    public void setSubtitle(CharSequence subtitle) {
//        if (getToolbar() != null)
//            getToolbar().setSubtitle(subtitle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log("onCreateView");
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
        Log("onCreate");
    }

    public void onStart() {
        Log("onStart");
        isStarted = true;
    }

    public void onNewIntent() {
        Log("onNewIntent");
    }

    public void onAttach(Activity activity) {
        isAdded = true;
        isPaused = false;
        isResumed = false;
        Log("onAttach");
    }

    public void onDetach() {
        isAdded = false;
        isPaused = false;
        isResumed = false;
        Log("onDetach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        Log("onActivityCreated");
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
            onDestroyView();
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
        Log("onBackPressed");
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
        isStopped = false;
        isStarted = true;
        mChildFragmentManager.onResume();
        Log("onResume");
    }

    public void onPause() {
        isResumed = false;
        isPaused = true;
        mChildFragmentManager.onPause();
        Log("onPause");
    }

    public void onStop() {
        Log("onStop");
        isStopped = true;
        isStarted = false;
        mChildFragmentManager.onStop();
    }

    public void onDestroyView() {
        Log("onDestroyView");
    }

    public void onDestroy() {
        mChildFragmentManager.onDestroy();
        clearViews();
        Log("onDestroy");
    }

    public void onOpenAnimationStart() {
        Log("onOpenAnimationStart");
    }

    public void onOpenAnimationEnd() {
        Log("onOpenAnimationEnd");
    }

    public void onPopAnimationStart() {
        Log("onPopAnimationStart");
    }

    public void onPopAnimationEnd() {
        Log("onPopAnimationEnd");
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            return;
        if (mData != null)
            outState.putBundle("mResultData", mData.getExtras());
        if (mArguments != null)
            outState.putBundle("mArguments", mArguments);
        mChildFragmentManager.onSaveInstanceState(outState);

        Log("onSaveInstanceState");
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

    public void onBeginSlide() {
        Log("onBeginSlide");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mChildFragmentManager != null) {
            mChildFragmentManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void Log(String message) {
        if (Utils.IS_DEBUGGING)
            android.util.Log.d(this.getClass().getSimpleName(), message);
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
