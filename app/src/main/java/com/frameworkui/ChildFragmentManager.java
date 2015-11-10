package com.frameworkui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/26/15.
 */
public class ChildFragmentManager {

    private ArrayList<BaseFragment> mChildFragments = new ArrayList<>();
    BaseFragmentActivity mActivity;
    Bundle mSavedInstanceState;
    private BaseFragment mCurrentFragment; //It's only use in main tab
    private Handler mHandlerUI = new Handler(Looper.getMainLooper());
    BaseFragment mParentFragment;

    public void showFragment(ViewGroup containerView, BaseFragment fragment) {
        showFragment(containerView, fragment, true, false);
    }

    public void showFragment(final ViewGroup containerView, final BaseFragment fragment, final boolean moveToResume, boolean checkAdded) {
        try {
            if (fragment == null || containerView == null) {
                return;
            }
            boolean itemAdded = false;
            if (checkAdded) {
                for (BaseFragment f : mChildFragments) {
                    itemAdded = f == fragment;
                    if (itemAdded)
                        break;
                }
            }
            if (!itemAdded) {
                mChildFragments.add(fragment);
            }
            fragment.mParentFragment = mParentFragment;
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
            if (moveToResume)
                fragment.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(final BaseFragment fragment) {
        try {
            if (fragment == null) {
                return;
            }
            if (!fragment.isPaused)
                fragment.onPause();
            if (!BaseFragment.SingleInstance.class.isInstance(fragment))
                fragment.onDestroy();
            else {
                ViewParent parent = fragment.getView().getParent();
                if (parent != null && ViewGroup.class.isInstance(parent)) {
                    try {
                        ((ViewGroup) parent).removeView(fragment.getView());
                        fragment.onDetach();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mChildFragments.remove(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        try {
            if (mCurrentFragment != null) {
                if (!mCurrentFragment.isResumed && mCurrentFragment.isAdded)
                    mCurrentFragment.onResume();
            } else {
                for (BaseFragment fragment : mChildFragments) {
                    if (!fragment.isResumed) {
                        fragment.onResume();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        for (BaseFragment fragment : mChildFragments) {
            if (!fragment.isPaused)
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

    public void setCurrentFragment(BaseFragment fragment) {
        this.mCurrentFragment = fragment;
    }

    public void detachFragmentView(BaseFragment fragment) {
        try {
            if (fragment == null) {
                return;
            }
            if (!fragment.isPaused)
                fragment.onPause();
            else {
                ViewParent parent = fragment.getView().getParent();
                if (parent != null && ViewGroup.class.isInstance(parent)) {
                    try {
                        ((ViewGroup) parent).removeView(fragment.getView());
                        fragment.onDetach();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mChildFragments != null && !mChildFragments.isEmpty()) {
            mChildFragments.get(mChildFragments.size() - 1).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
