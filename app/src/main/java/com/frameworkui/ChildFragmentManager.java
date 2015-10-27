package com.frameworkui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/26/15.
 */
public class ChildFragmentManager {

    private ArrayList<BaseFragment> mChildFragments = new ArrayList<>();
    BaseFragmentActivity mActivity;
    Bundle mSavedInstanceState;

    public void showFragment(ViewGroup containerView, BaseFragment fragment) {
        if (fragment == null || containerView == null) {
            return;
        }
        mChildFragments.add(fragment);
        fragment.setActivity(mActivity);
        fragment.onCreate(mSavedInstanceState);
        fragment.onActivityCreated(mSavedInstanceState);
        View fragmentView = fragment.mFragmentView;
        if (fragmentView == null) {
            fragmentView = fragment.onCreateView(mActivity.getLayoutInflater(), containerView, mSavedInstanceState);
            fragment.onViewCreated(fragmentView, mSavedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        containerView.addView(fragmentView);
        fragment.onAttach(mActivity);
        ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        fragmentView.setLayoutParams(layoutParams);
        fragment.onResume();
    }

    public void onResume() {
        for (BaseFragment fragment : mChildFragments) {
            if (!fragment.isResumed) {
                fragment.onResume();
            }
        }
    }

    public void onPause() {
        for (BaseFragment fragment : mChildFragments) {
            fragment.onPause();
        }
    }

    public void onDestroy() {
        for (BaseFragment fragment : mChildFragments) {
            fragment.onDestroy();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        for (BaseFragment fragment : mChildFragments) {
            fragment.onSaveInstanceState(outState);
        }
    }

}
