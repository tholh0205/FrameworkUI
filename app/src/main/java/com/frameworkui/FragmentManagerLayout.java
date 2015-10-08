package com.frameworkui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/7/15.
 */
public class FragmentManagerLayout extends FrameLayout {

    private ArrayList<FragmentData.FragmentItem> mFragmentStack = new ArrayList<>();

    private LinearLayout mContainerViewBack, mContainerView;

    public FragmentManagerLayout(Context context) {
        super(context);
    }

    public FragmentManagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentManagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FragmentManagerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BaseFragmentActivity getBaseActivity() {
        return (BaseFragmentActivity) getContext();
    }

    public void init(ArrayList<FragmentData.FragmentItem> stack) {
        mContainerViewBack = new LinearLayout(getContext());
        addView(mContainerViewBack);
        LayoutParams layoutParams = (LayoutParams) mContainerViewBack.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mContainerViewBack.setLayoutParams(layoutParams);

        mContainerView = new LinearLayout(getContext());
        addView(mContainerView);
        layoutParams = (LayoutParams) mContainerView.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mContainerView.setLayoutParams(layoutParams);

        if (stack != null && !stack.isEmpty()) {
            mFragmentStack = stack;
        }
    }

    public void onResume() {
        try {
            if (mFragmentStack.size() > 0) {
                mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onResume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        try {
            if (mFragmentStack.size() > 0) {
                mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onPause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        onTouchEvent(null);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean onBackPressed() {
        return goToBackStack(true);
    }

    public void onLowMemory() {

    }

    public void recreateAllFragmentViews(boolean includeLast) {
        //includeLast ? 0 : 1
    }

    public void showFragment(FragmentData.FragmentType fragmentType, Bundle data, int requestCode, boolean noAnimation, final boolean removelast) {
        try {
            if (FragmentAnimationUtils.isRunning())
                return;

            boolean needAnimation = noAnimation ? false : true;

            final FragmentData.FragmentItem currentFragmentItem = mFragmentStack.isEmpty() ? null : mFragmentStack.get(mFragmentStack.size() - 1);

            FragmentData.FragmentItem fragmentItem = null;
            if (fragmentItem == null) {
                fragmentItem = new FragmentData.FragmentItem(fragmentType, data);
                mFragmentStack.add(fragmentItem);
            }
            fragmentItem.mFragment = fragmentType.getFragmentClass().newInstance();

            if (requestCode > 0) {
                fragmentItem.mRequestCode = requestCode;
            }
            fragmentItem.getFragment().setActivity(getBaseActivity());
            View fragmentView = fragmentItem.mFragment.onCreateView(getContext(), null);
            fragmentItem.mFragment.onViewCreated(fragmentView);
            mContainerViewBack.addView(fragmentView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            fragmentView.setLayoutParams(layoutParams);
            fragmentItem.getFragment().onResume();
            fragmentItem.getFragment().onSetupActionBar();

            LinearLayout temp = mContainerView;
            mContainerView = mContainerViewBack;
            mContainerViewBack = temp;
            mContainerView.setVisibility(View.VISIBLE);
            ViewHelper.setTranslationX(mContainerView, 0f);
            ViewHelper.setTranslationX(mContainerViewBack, 0f);
            bringChildToFront(mContainerView);

            if (!needAnimation || currentFragmentItem == null) {
                showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                fragmentItem.getFragment().onOpenAnimationStart();
                fragmentItem.getFragment().onOpenAnimationEnd();
            } else {
                final BaseFragment fragment = fragmentItem.getFragment();
                fragment.onOpenAnimationStart();
                FragmentAnimationUtils.openTranslationWithFadeIn(fragmentView, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                        fragment.onOpenAnimationEnd();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                        fragment.onOpenAnimationEnd();
                    }
                });
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void showFragmentInternalRemoveOld(boolean removeLastFromStack, FragmentData.FragmentItem fragmentItem) {
        if (fragmentItem == null || fragmentItem.getFragment() == null) {
            return;
        }
        BaseFragment fragment = fragmentItem.getFragment();
        fragment.onPause();
        if (removeLastFromStack) {
            fragment.onDestroy();
            fragment.setActivity(null);
            mFragmentStack.remove(fragmentItem);
        } else {
            if (fragment.mFragmentView != null) {
                ViewGroup parent = (ViewGroup) fragment.mFragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragment.mFragmentView);
                }
            }
        }
        mContainerViewBack.setVisibility(View.GONE);
    }

    public boolean goToBackStack(boolean animated) {
        try {
            if (FragmentAnimationUtils.isRunning())
                return true;
            if (mFragmentStack == null || mFragmentStack.size() < 2) {
                return false;
            }
            final FragmentData.FragmentItem topFragmentItem = mFragmentStack.get(mFragmentStack.size() - 1);
            final FragmentData.FragmentItem belowFragmentItem = mFragmentStack.get(mFragmentStack.size() - 2);

            if (belowFragmentItem != null) {
                LinearLayout temp = mContainerView;
                mContainerView = mContainerViewBack;
                mContainerViewBack = temp;
                mContainerView.setVisibility(View.VISIBLE);

                if (belowFragmentItem.mFragment == null) {
                    belowFragmentItem.mFragment = belowFragmentItem.cls.newInstance();
                }

                belowFragmentItem.mFragment.setActivity(getBaseActivity());
                View fragmentView = belowFragmentItem.getFragment().mFragmentView;
                if (fragmentView == null) {
                    fragmentView = belowFragmentItem.getFragment().onCreateView(getContext(), null);
                    belowFragmentItem.getFragment().onViewCreated(fragmentView);
                } else {
                    ViewGroup parent = (ViewGroup) fragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(fragmentView);
                    }
                }
                mContainerView.addView(fragmentView);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                fragmentView.setLayoutParams(layoutParams);
                belowFragmentItem.getFragment().onOpenAnimationStart();
                belowFragmentItem.getFragment().onResume();
                belowFragmentItem.getFragment().onSetupActionBar();
                if (animated) {
                    FragmentAnimationUtils.closeTranslationWithFadeIn(topFragmentItem.getFragment().mFragmentView, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            closeLastFragmentInternalRemoveOld(topFragmentItem);
                            belowFragmentItem.getFragment().onOpenAnimationEnd();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            closeLastFragmentInternalRemoveOld(topFragmentItem);
                            belowFragmentItem.getFragment().onOpenAnimationEnd();
                        }
                    });
                } else {
                    closeLastFragmentInternalRemoveOld(topFragmentItem);
                    belowFragmentItem.getFragment().onOpenAnimationEnd();
                }
            }

            return true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void closeLastFragmentInternalRemoveOld(FragmentData.FragmentItem fragmentItem) {
        BaseFragment fragment = fragmentItem.getFragment();
        fragment.onPause();
        fragment.onDestroy();
        fragment.setActivity(getBaseActivity());
        mFragmentStack.remove(fragmentItem);
        mContainerViewBack.setVisibility(View.GONE);
        bringChildToFront(mContainerView);
    }

}
