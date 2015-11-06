package com.frameworkui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/7/15.
 */
public class FragmentManagerLayout extends FrameLayout {

    private ArrayList<FragmentData.FragmentItem> mFragmentStack = new ArrayList<>();
    private ArrayList<FragmentData.FragmentItem> mReusableList = new ArrayList<>();

    private LinearLayout mContainerViewBack, mContainerView;
    private Bundle mSavedInstanceState;
    private boolean mAnimationInProgress = false;
    private boolean mStartedTracking = false;
    private boolean mMaybeStartTracking = false;
    private int mStartedTrackingX;
    private int mStartedTrackingY;
    private boolean mBeginTrackingSent = false;
    private VelocityTracker mVelocityTracker;
    private int mStartedTrackingPointerId;

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
        mContainerViewBack = new InterceptTouchLinearLayout(getContext());
        addView(mContainerViewBack);
        LayoutParams layoutParams = (LayoutParams) mContainerViewBack.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mContainerViewBack.setLayoutParams(layoutParams);

        mContainerView = new InterceptTouchLinearLayout(getContext());
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
        return !(!mAnimationInProgress) || onTouchEvent(ev);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        android.util.Log.d("ThoLH", "requestDisallowInterceptTouchEvent " + disallowIntercept);
        onTouchEvent(null);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!mAnimationInProgress) {
            if (mFragmentStack.size() > 1) {
                if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN && !mStartedTracking && !mMaybeStartTracking) {
                    BaseFragment currentFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
                    if (!currentFragment.isEnableSwipeBack()) {
                        return false;
                    }
                    mStartedTrackingPointerId = ev.getPointerId(0);
                    mMaybeStartTracking = true;
                    mStartedTrackingX = (int) ev.getX();
                    mStartedTrackingY = (int) ev.getY();
                    if (mVelocityTracker != null) {
                        mVelocityTracker.clear();
                    }
                } else if (ev != null && ev.getAction() == MotionEvent.ACTION_MOVE && ev.getPointerId(0) == mStartedTrackingPointerId) {
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    int dx = Math.max(0, (int) (ev.getX() - mStartedTrackingX));
                    int dy = Math.abs((int) ev.getY() - mStartedTrackingY);
                    mVelocityTracker.addMovement(ev);
                    if (mMaybeStartTracking && !mStartedTracking && dx >= Utils.getPixelsInCM(0.4f, true) && Math.abs(dx) / 3 > dy) {
                        prepareForMoving(ev);
                    } else if (mStartedTracking) {
                        if (!mBeginTrackingSent) {
                            if (getBaseActivity().getCurrentFocus() != null) {
//                                AndroidUtilities.hideKeyboard(parentActivity.getCurrentFocus());
                            }
//                            BaseFragment currentFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//                            currentFragment.onBeginSlide();
                            mBeginTrackingSent = true;
                        }
                        ViewHelper.setTranslationX(mContainerView, dx);
//                        ViewHelper.setTranslationX(mContainerViewBack, -(getResources().getDisplayMetrics().widthPixels - dx) / 2);
//                        setInnerTranslationX(dx);
                    }
                } else if (ev != null && ev.getPointerId(0) == mStartedTrackingPointerId && (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_POINTER_UP)) {
                    if (mVelocityTracker == null) {
                        mVelocityTracker = VelocityTracker.obtain();
                    }
                    mVelocityTracker.computeCurrentVelocity(1000);
                    if (!mStartedTracking && mFragmentStack.get(mFragmentStack.size() - 1).getFragment().isEnableSwipeBack()) {
                        float velX = mVelocityTracker.getXVelocity();
                        float velY = mVelocityTracker.getYVelocity();
                        if (velX >= 3500 && velX > Math.abs(velY)) {
                            prepareForMoving(ev);
                            if (!mBeginTrackingSent) {
                                if (((Activity) getContext()).getCurrentFocus() != null) {
//                                    AndroidUtilities.hideKeyboard(((Activity) getContext()).getCurrentFocus());
                                }
                                mBeginTrackingSent = true;
                            }
                        }
                    }
                    if (mStartedTracking) {
                        float x = ViewHelper.getX(mContainerView);
                        AnimatorSet animatorSet = new AnimatorSet();
                        float velX = mVelocityTracker.getXVelocity();
                        float velY = mVelocityTracker.getYVelocity();
                        final boolean backAnimation = x < mContainerView.getMeasuredWidth() / 3.0f && (velX < 3500 || velX < velY);
                        float distToMove;
                        if (!backAnimation) {
                            distToMove = mContainerView.getMeasuredWidth() - x;
                            animatorSet.playTogether(
                                    ObjectAnimator.ofFloat(mContainerView, "translationX", mContainerView.getMeasuredWidth())
//                                    ObjectAnimatorProxy.ofFloat(this, "innerTranslationX", (float) mContainerView.getMeasuredWidth())
                            );
                        } else {
                            distToMove = x;
                            animatorSet.playTogether(
                                    ObjectAnimator.ofFloat(mContainerView, "translationX", 0)
//                                    ObjectAnimatorProxy.ofFloat(this, "innerTranslationX", 0.0f)
                            );
                        }

                        animatorSet.setDuration(Math.max((int) (200.0f / mContainerView.getMeasuredWidth() * distToMove), 50));
                        animatorSet.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                onSlideAnimationEnd(backAnimation);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                super.onAnimationCancel(animation);
                                onSlideAnimationEnd(backAnimation);
                            }
                        });
                        animatorSet.start();
                        mAnimationInProgress = true;
                    } else {
                        mMaybeStartTracking = false;
                        mStartedTracking = false;
                    }
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                } else if (ev == null) {
                    mMaybeStartTracking = false;
                    mStartedTracking = false;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }
            }
            return mStartedTracking;
        }
        return false;
    }

    private void prepareForMoving(MotionEvent ev) {
        mMaybeStartTracking = false;
        mStartedTracking = true;
        mStartedTrackingX = (int) ev.getX();
        mContainerViewBack.setVisibility(View.VISIBLE);
        mBeginTrackingSent = false;

        BaseFragment lastFragment = mFragmentStack.get(mFragmentStack.size() - 2).getFragment();
        View fragmentView = lastFragment.mFragmentView;
        if (fragmentView == null) {
            fragmentView = lastFragment.onCreateView(getBaseActivity().getLayoutInflater(), null, mSavedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        mContainerViewBack.addView(fragmentView);
        lastFragment.onAttach(getBaseActivity());
        ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        fragmentView.setLayoutParams(layoutParams);

        lastFragment.onResume();
        lastFragment.onSetupActionBar();

        //AndroidUtilities.lockOrientation(parentActivity);
    }

    private void onSlideAnimationEnd(final boolean backAnimation) {
        boolean isKeepBelowItem = BaseFragment.KeepBelowFragment.class.isInstance(mFragmentStack.get(mFragmentStack.size() - 1).getFragment());
        if (!backAnimation) {
            BaseFragment lastFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
            lastFragment.onPause();
            if (BaseFragment.ReusableFragment.class.isInstance(lastFragment)) {
                if (lastFragment.mFragmentView != null) {
                    ViewGroup parent = (ViewGroup) lastFragment.mFragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(lastFragment.mFragmentView);
                        lastFragment.onDetach();
                    }
                }
                mReusableList.add(mFragmentStack.get(mFragmentStack.size() - 1));
            } else {
                lastFragment.onDestroy();
                lastFragment.setActivity(null);
            }
            mFragmentStack.remove(mFragmentStack.size() - 1);

            LinearLayout temp = mContainerView;
            mContainerView = mContainerViewBack;
            mContainerViewBack = temp;
            mContainerView.setVisibility(View.VISIBLE);
            bringChildToFront(mContainerView);

            lastFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
            lastFragment.onResume();
            lastFragment.onSetupActionBar();
        } else {
            BaseFragment lastFragment = mFragmentStack.get(mFragmentStack.size() - 2).getFragment();
            lastFragment.onPause();
            if (lastFragment.mFragmentView != null && !isKeepBelowItem) {
                ViewGroup parent = (ViewGroup) lastFragment.mFragmentView.getParent();
                if (parent != null) {
                    parent.removeView(lastFragment.mFragmentView);
                    lastFragment.onDetach();
                }
            }
        }
        if (!isKeepBelowItem)
            mContainerViewBack.setVisibility(View.GONE);
        //AndroidUtilities.unlockOrientation(parentActivity);
        mStartedTracking = false;
        mAnimationInProgress = false;

        ViewHelper.setTranslationX(mContainerView, 0);
        ViewHelper.setTranslationX(mContainerViewBack, 0);
    }

    public boolean onBackPressed() {
        if (mStartedTracking) {
            return true;
        }
        if (!mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onBackPressed()) {
                return true;
            }
        }
        return goToBackStack(true);
    }

    public void onLowMemory() {
        if (!mFragmentStack.isEmpty()) {
            for (FragmentData.FragmentItem fragmentItem : mFragmentStack) {
                if (fragmentItem.getFragment() != null) {
                    fragmentItem.getFragment().onLowMemory();
                }
            }
        }
    }

    public void recreateAllFragmentViews(boolean includeLast) {
        //includeLast ? 0 : 1
    }

    public void showFragment(FragmentData.FragmentType fragmentType, Bundle data, int requestCode, boolean noAnimation, final boolean removeLast) {
        try {
            if (FragmentAnimationUtils.isRunning())
                return;
            if (data == null) {
                data = new Bundle();
            }

            boolean needAnimation = noAnimation ? false : true;
            boolean isSingleItem = false;
            boolean isReusableItem = false;

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

            if (fragmentItem == null && !mReusableList.isEmpty()) {
                for (FragmentData.FragmentItem item : mReusableList) {
                    if (item.mFragmentType.getTypeId() == fragmentType.getTypeId()) {
                        fragmentItem = item;
                        isSingleItem = false;
                        isReusableItem = true;
                        break;
                    }
                }
            }

            if (fragmentItem == null) {
                fragmentItem = new FragmentData.FragmentItem(fragmentType, data);
            }

            if (!isSingleItem || isReusableItem) {
                mFragmentStack.add(fragmentItem);
                mReusableList.remove(fragmentItem);
            }

            if (fragmentItem.getFragment() == null) {
                fragmentItem.mFragment = fragmentType.getFragmentClass().newInstance();
                fragmentItem.mFragment.setArguments(data);
                fragmentItem.mFragment.onCreate(mSavedInstanceState);
                fragmentItem.mFragment.onActivityCreated(mSavedInstanceState);
            } else {
                fragmentItem.mFragment.setArguments(data);
                fragmentItem.mFragment.onNewIntent();
            }

            final boolean isKeepBelowItem = BaseFragment.KeepBelowFragment.class.isInstance(fragmentItem.getFragment());

//            fragmentItem.mFragment.setArguments(data);

//            if (requestCode > 0) {
            fragmentItem.mRequestCode = requestCode;
//            }
            fragmentItem.getFragment().setActivity(getBaseActivity());
            View fragmentView = fragmentItem.getFragment().mFragmentView;
            if (fragmentView == null) {
                fragmentView = fragmentItem.mFragment.onCreateView(getBaseActivity().getLayoutInflater(), null, mSavedInstanceState);
                if (isKeepBelowItem) {
                    fragmentView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
                fragmentItem.mFragment.onViewCreated(fragmentView, mSavedInstanceState);
            } else {
                ViewGroup parent = (ViewGroup) fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragmentView);
                }
            }
            mContainerViewBack.addView(fragmentView);
            fragmentItem.mFragment.onAttach(getBaseActivity());
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
                if (!isKeepBelowItem) {
                    if (isSingleItem) {
                        showFragmentInternalRemoveFromSingleInstance(fragmentItem);
                    } else {
                        showFragmentInternalRemoveOld(removeLast, currentFragmentItem);
                    }
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
                        if (!isKeepBelowItem) {
                            if (singleFragmentItem != null) {
                                showFragmentInternalRemoveFromSingleInstance(singleFragmentItem);
                            } else {
                                showFragmentInternalRemoveOld(removeLast, currentFragmentItem);
                            }
                        }
                        fragment.onOpenAnimationEnd();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (!isKeepBelowItem) {
                            if (singleFragmentItem != null) {
                                showFragmentInternalRemoveFromSingleInstance(singleFragmentItem);
                            } else {
                                showFragmentInternalRemoveOld(removeLast, currentFragmentItem);
                            }
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
                i--;
            }
        }
    }

    private void showFragmentInternalRemoveOld(boolean removeLastFromStack, FragmentData.FragmentItem fragmentItem) {
        if (fragmentItem == null || fragmentItem.getFragment() == null) {
            return;
        }
        BaseFragment fragment = fragmentItem.getFragment();
        fragment.onPause();
        MainApplication.getInstance().runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (getBaseActivity() != null)
                    getBaseActivity().supportInvalidateOptionsMenu();
            }
        });
        if (removeLastFromStack) {
            fragment.onDestroy();
            fragment.setActivity(null);
            mFragmentStack.remove(fragmentItem);
        } else {
            if (fragment.mFragmentView != null) {
                ViewGroup parent = (ViewGroup) fragment.mFragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragment.mFragmentView);
                    fragment.onDetach();
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
                fragmentItem.mFragment.setArguments(fragmentItem.mData);
                fragmentItem.mFragment.onCreate(mSavedInstanceState);
                fragmentItem.mFragment.onActivityCreated(mSavedInstanceState);
            }

            fragmentItem.getFragment().setActivity(getBaseActivity());
            View fragmentView = fragmentItem.getFragment().mFragmentView;
            if (fragmentView == null) {
                fragmentView = fragmentItem.mFragment.onCreateView(getBaseActivity().getLayoutInflater(), null, mSavedInstanceState);
                fragmentItem.mFragment.onViewCreated(fragmentView, mSavedInstanceState);
            } else {
                ViewGroup parent = (ViewGroup) fragmentView.getParent();
                if (parent != null) {
                    parent.removeView(fragmentView);
                }
            }
            mContainerViewBack.addView(fragmentView);
            fragmentItem.mFragment.onAttach(getBaseActivity());
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
            if (topFragmentItem.getFragment() != null) {
                topFragmentItem.getFragment().isRemoving = true;
            }
            if (belowFragmentItem != null) {
                LinearLayout temp = mContainerView;
                mContainerView = mContainerViewBack;
                mContainerViewBack = temp;
                mContainerView.setVisibility(View.VISIBLE);

                if (belowFragmentItem.mFragment == null) {
                    belowFragmentItem.mFragment = belowFragmentItem.cls.newInstance();
                    belowFragmentItem.mFragment.setArguments(belowFragmentItem.mData);
                    belowFragmentItem.mFragment.onCreate(mSavedInstanceState);
                    belowFragmentItem.mFragment.onActivityCreated(mSavedInstanceState);
                }

                belowFragmentItem.mFragment.setActivity(getBaseActivity());
                View fragmentView = belowFragmentItem.getFragment().mFragmentView;
                if (fragmentView == null) {
                    android.util.Log.d("ThoLH", "goToBackStack belowFragment fragmentView == null");
                    fragmentView = belowFragmentItem.getFragment().onCreateView(getBaseActivity().getLayoutInflater(), null, mSavedInstanceState);
                    belowFragmentItem.getFragment().onViewCreated(fragmentView, mSavedInstanceState);
                } else {
                    ViewGroup parent = (ViewGroup) fragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(fragmentView);
                    }
                }
                mContainerView.addView(fragmentView);
                belowFragmentItem.mFragment.onAttach(getBaseActivity());
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragmentView.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
                fragmentView.setLayoutParams(layoutParams);
                if (!belowFragmentItem.getFragment().isResumed) {
                    belowFragmentItem.getFragment().onResume();
                    belowFragmentItem.getFragment().onSetupActionBar();
                }
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
        MainApplication.getInstance().runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (getBaseActivity() != null)
                    getBaseActivity().supportInvalidateOptionsMenu();
            }
        });
        if (!BaseFragment.ReusableFragment.class.isInstance(fragment)) {
            fragment.onDestroy();
            fragment.setActivity(null);
        } else {
            ViewGroup parent = (ViewGroup) fragment.mFragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragment.mFragmentView);
                fragment.onDetach();
            }
            mReusableList.add(fragmentItem);
        }

        mFragmentStack.remove(fragmentItem);
        fragment.isRemoving = false;
        fragment.isFinished = true;
        mContainerViewBack.setVisibility(View.GONE);
        bringChildToFront(mContainerView);
        mContainerViewBack.removeAllViews();
        resetAnimationCheck(false);
        if (fragmentItem.mRequestCode > 0) {
            onActivityResult(fragmentItem.mRequestCode, fragment.mResultCode, fragment.mData);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mFragmentStack.isEmpty()) {
            mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onActivityResult(requestCode, resultCode, data);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).getFragment().hasMenu) {
                mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onCreateOptionsMenu(menu, inflater);
            }
        }
    }

    public boolean onOptionItemSelected(MenuItem item) {
        if (!mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).getFragment().onOptionItemSelected(item)) {
                return true;
            }
        }
        return false;
    }

    public void onSaveInstanceState(Bundle outState) {
        if (!mFragmentStack.isEmpty()) {
            for (FragmentData.FragmentItem fragmentItem : mFragmentStack) {
                if (fragmentItem.getFragment() != null) {
                    fragmentItem.getFragment().onSaveInstanceState(outState);
                }
            }
        }
    }

    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
    }

    public void onDestroy() {
        if (mContainerView != null) {
            mContainerView.removeAllViews();
            mContainerView = null;
        }
        if (mContainerViewBack != null) {
            mContainerViewBack.removeAllViews();
            mContainerViewBack = null;
        }
        this.removeAllViews();
    }

}
