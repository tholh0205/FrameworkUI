package com.frameworkui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/27/15.
 */
public abstract class BaseFragmentPagerAdapter extends PagerAdapter {

    private ChildFragmentManager childFragmentManager;
    private int mCurrentPosition = 0;

    public BaseFragmentPagerAdapter(ChildFragmentManager childFragmentManager) {
        this.childFragmentManager = childFragmentManager;
    }

    public abstract BaseFragment getItem(int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseFragment fragment = getItem(position);
        if (container.getChildCount() == 0) {
            childFragmentManager.showFragment(container, fragment, true);
        } else {
            childFragmentManager.showFragment(container, fragment, false);
        }
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (position != mCurrentPosition) {
            BaseFragment fragment = (BaseFragment) object;
            BaseFragment current = getItem(mCurrentPosition);
            if (current != fragment) {
                if (!current.isPaused) {
                    current.onPause();
                }
                if (!fragment.isResumed) {
                    fragment.onResume();
                }
                mCurrentPosition = position;
                childFragmentManager.setCurrentFragment(fragment);
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((BaseFragment) object).getView() == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof BaseFragment) {
            childFragmentManager.remove((BaseFragment) object);
        }
    }

}
