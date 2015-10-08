package com.frameworkui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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

    public ArrayList<FragmentData.FragmentItem> getFragmentStack() {
        return mFragmentStack;
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
        if (!mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onBackPressed()) {
                return true;
            }
        }
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
            if (data == null) {
                data = new Bundle();
            }

            boolean needAnimation = noAnimation ? false : true;
            boolean isSingleItem = false;

            final FragmentData.FragmentItem currentFragmentItem = mFragmentStack.isEmpty() ? null : mFragmentStack.get(mFragmentStack.size() - 1);

            FragmentData.FragmentItem fragmentItem = null;
            //Find single instance fragment item
            for (int i = 0; i < mFragmentStack.size(); i++) {
                FragmentData.FragmentItem item = mFragmentStack.get(i);
                if (BaseFragment.SingleInstance.class.isInstance(item.getFragment()) && item.mFragmentType.getTypeId() == fragmentType.getTypeId()) {
                    fragmentItem = item;
                    isSingleItem = true;
                    break;
                }
            }

            if (fragmentItem == null) {
                fragmentItem = new FragmentData.FragmentItem(fragmentType, data);
                mFragmentStack.add(fragmentItem);
            }
            if (fragmentItem.getFragment() == null) {
                fragmentItem.mFragment = fragmentType.getFragmentClass().newInstance();
            }
            fragmentItem.mFragment.setArguments(data);

//            if (requestCode > 0) {
            fragmentItem.mRequestCode = requestCode;
//            }
            fragmentItem.getFragment().setActivity(getBaseActivity());
            View fragmentView = fragmentItem.getFragment().mFragmentView;
            if (fragmentView == null) {
                fragmentView = fragmentItem.mFragment.onCreateView(getContext(), null);
                fragmentItem.mFragment.onViewCreated(fragmentView);
            } else {
                ViewGroup parent = (ViewGroup) fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragmentView);
                }
            }
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
            bringChildToFront(mContainerView);

            if (!needAnimation || currentFragmentItem == null) {
                if (isSingleItem) {
                    showFragmentInternalRemoveFromSingleInstance(fragmentItem);
                } else {
                    showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                }
                fragmentItem.getFragment().onOpenAnimationStart();
                fragmentItem.getFragment().onOpenAnimationEnd();
            } else {
                final BaseFragment fragment = fragmentItem.getFragment();
                final FragmentData.FragmentItem singleFragmentItem = isSingleItem ? fragmentItem : null;
                fragment.onOpenAnimationStart();
                FragmentAnimationUtils.openTranslationWithFadeIn(mContainerView, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        if (singleFragmentItem != null) {
                            showFragmentInternalRemoveFromSingleInstance(singleFragmentItem);
                        } else {
                            showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                        }
                        fragment.onOpenAnimationEnd();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (singleFragmentItem != null) {
                            showFragmentInternalRemoveFromSingleInstance(singleFragmentItem);
                        } else {
                            showFragmentInternalRemoveOld(removelast, currentFragmentItem);
                        }
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

    private void showFragmentInternalRemoveFromSingleInstance(FragmentData.FragmentItem singleInstanceItem) {
        if (singleInstanceItem == null)
            return;
        int index = mFragmentStack.indexOf(singleInstanceItem);
        if (index > 0 && index < mFragmentStack.size() - 1) {
            for (int i = index + 1; i < mFragmentStack.size(); i++) {
                showFragmentInternalRemoveOld(true, mFragmentStack.get(i));
            }
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
        resetAnimationCheck(false);
    }

    public void showLastFragment() {
        try {
            final FragmentData.FragmentItem fragmentItem = mFragmentStack.isEmpty() ? null : mFragmentStack.get(mFragmentStack.size() - 1);
            if (fragmentItem.getFragment() == null) {
                fragmentItem.mFragment = fragmentItem.cls.newInstance();
            }
            fragmentItem.mFragment.setArguments(fragmentItem.mData);

            fragmentItem.getFragment().setActivity(getBaseActivity());
            View fragmentView = fragmentItem.getFragment().mFragmentView;
            if (fragmentView == null) {
                fragmentView = fragmentItem.mFragment.onCreateView(getContext(), null);
                fragmentItem.mFragment.onViewCreated(fragmentView);
            } else {
                ViewGroup parent = (ViewGroup) fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragmentView);
                }
            }
            mContainerViewBack.addView(fragmentView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            fragmentView.setLayoutParams(layoutParams);
            fragmentItem.getFragment().onResume();
            MainApplication.getInstance().runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (fragmentItem != null && fragmentItem.getFragment() != null) {
                        fragmentItem.getFragment().onSetupActionBar();
                    }
                }
            });

            LinearLayout temp = mContainerView;
            mContainerView = mContainerViewBack;
            mContainerViewBack = temp;
            mContainerView.setVisibility(View.VISIBLE);
            bringChildToFront(mContainerView);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
                    belowFragmentItem.mFragment.setArguments(belowFragmentItem.mData);
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
                belowFragmentItem.getFragment().onResume();
                belowFragmentItem.getFragment().onSetupActionBar();
                belowFragmentItem.getFragment().onOpenAnimationStart();
                if (animated) {
                    FragmentAnimationUtils.closeTranslationWithFadeIn(mContainerViewBack, new AnimatorListenerAdapter() {
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
        if (!BaseFragment.SingleInstance.class.isInstance(fragment)) {
            fragment.onDestroy();
        } else {
            ViewGroup parent = (ViewGroup) fragment.mFragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragment.mFragmentView);
            }
        }
        fragment.setActivity(null);
        mFragmentStack.remove(fragmentItem);
        mContainerViewBack.setVisibility(View.GONE);
        bringChildToFront(mContainerView);
        resetAnimationCheck(false);
        if (fragmentItem.mRequestCode > 0) {
            onActivityResultFragment(fragmentItem.mRequestCode, fragment.mResultCode, fragment.mData);
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (!mFragmentStack.isEmpty()) {
            mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onActivityResultFragment(requestCode, resultCode, data);
        }
    }

    private void resetAnimationCheck(boolean byCheck) {
        if (byCheck) {
            FragmentAnimationUtils.cancelCurrentAnimation();
        }
        ViewHelper.setAlpha(this, 1f);
        ViewHelper.setAlpha(mContainerView, 1f);
        ViewHelper.setScaleX(mContainerView, 1f);
        ViewHelper.setScaleY(mContainerView, 1f);
        ViewHelper.setTranslationX(mContainerView, 0f);
        ViewHelper.setTranslationY(mContainerView, 0f);
        ViewHelper.setAlpha(mContainerViewBack, 1f);
        ViewHelper.setScaleX(mContainerViewBack, 1f);
        ViewHelper.setScaleY(mContainerViewBack, 1f);
        ViewHelper.setTranslationX(mContainerViewBack, 0f);
        ViewHelper.setTranslationY(mContainerViewBack, 0f);
    }

}
