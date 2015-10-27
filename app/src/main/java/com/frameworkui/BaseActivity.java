package com.frameworkui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ThoLH on 10/02/2015.
 */
public class BaseActivity extends AppCompatActivity {
//    public static final int NO_ANIMATION = -1, FADE_IN = 0, TRANSLATION = 1, TRANSLATION_WITH_FADE_IN = 2;
//
//    private final String FRAGMENT_STACK_KEY = "FRAGMENT_STACK_KEY";
//
//    private ViewGroup mContainerView;
//    private ArrayList<FragmentItem> mFragmentStack = null;
//    private int mSingleInstanceFragmentIndex = -1;
//    private volatile boolean isCreating = false, isShowingFragment = false, isPoppingStack = false;
//    private Bundle mSavedInstanceState = null;
//    private Handler mHandler = new Handler();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        mSavedInstanceState = savedInstanceState;
//        super.onCreate(savedInstanceState);
//        isCreating = true;
//        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
//        setContentView(R.layout.activity_main);
//        mContainerView = (ViewGroup) findViewById(R.id.fragment_container);
//        mFragmentStack = new ArrayList<>();
//        try {
//            if (savedInstanceState != null) {
//                ArrayList<Parcelable> parcelables = savedInstanceState.containsKey(FRAGMENT_STACK_KEY) ? savedInstanceState.getParcelableArrayList(FRAGMENT_STACK_KEY) : null;
//                if (parcelables != null && parcelables.size() > 0) {
//                    mFragmentStack.clear();
//                    for (Parcelable parcelable : parcelables) {
//                        mFragmentStack.add((FragmentItem) parcelable);
//                    }
//                    showLastFragment(savedInstanceState);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!isCreating) {
//            if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
//                BaseFragment fragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//                if (fragment != null) {
//                    fragment.onResume();
//                }
//            }
//        } else {
//            isCreating = false;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
//            BaseFragment fragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//            if (fragment != null) {
//                fragment.onPause();
//            }
//        }
//    }
//
//    public boolean presentFragment(FragmentType fragmentType, Bundle arguments) {
//        return presentFragment(fragmentType, arguments, -1, FADE_IN);
//    }
//
//    public boolean presentFragment(FragmentType fragmentType, Bundle arguments, int requestCode, int animationType) {
//        return presentFragment(fragmentType, arguments, requestCode, animationType, false);
//    }
//
//    public boolean presentFragment(FragmentType fragmentType, Bundle arguments, int requestCode, int animationType, final boolean removeLast) {
//        try {
//            if (isShowingFragment) return true;
//            isShowingFragment = true;
//            if (arguments == null) {
//                arguments = new Bundle();
//            }
//            boolean allowClearTopFromSingleInstance = getResources().getBoolean(R.bool.clear_top_from_single_instance_item);
//            boolean hasSingleInstanceItem = false;
//            FragmentItem fragmentItem = null;
//            if (mSingleInstanceFragmentIndex > -1 && mSingleInstanceFragmentIndex < mFragmentStack.size()) {
//                //TODO: handle single instance case
//                if (fragmentType.getTypeId() == mFragmentStack.get(mSingleInstanceFragmentIndex).fragmentType.getTypeId()) {
//                    fragmentItem = mFragmentStack.get(mSingleInstanceFragmentIndex);
//                    if (allowClearTopFromSingleInstance) {
//                        hasSingleInstanceItem = true;
//                    } else {
//                        mFragmentStack.remove(mSingleInstanceFragmentIndex);
//                        mSingleInstanceFragmentIndex = mFragmentStack.size();
//                    }
//                }
//            }
//            if (fragmentItem == null) {
//                fragmentItem = new FragmentItem(fragmentType, arguments);
//                fragmentItem.fragment = fragmentType.getFragmentClass().newInstance();
//                if (BaseFragment.SingleInstance.class.isInstance(fragmentItem.fragment)) {
//                    mSingleInstanceFragmentIndex = mFragmentStack.size();
//                }
//            }
//            fragmentItem.fragment.setArguments(arguments);
//            if (requestCode > 0 && !hasSingleInstanceItem)
//                fragmentItem.fragment.setRequestCode(requestCode);
//
//            boolean needAnimation = animationType != NO_ANIMATION && getResources().getBoolean(R.bool.use_view_animation);
//            final BaseFragment fragment = fragmentItem.fragment;
//            fragment.setBaseActivity(this);
//            View fragmentView = fragment.mFragmentView;
//
//            if (fragmentView == null) {
//                fragmentView = fragment.onCreateView(this, mContainerView);
//            } else {
//                ViewGroup parent = (ViewGroup) fragmentView.getParent();
//                if (parent != null) {
//                    parent.removeView(fragmentView);
//                }
//            }
//            mContainerView.addView(fragmentView);
//            ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            fragmentView.setLayoutParams(layoutParams);
//            fragmentView.bringToFront();
//
//            final FragmentItem currentFragmentItem = hasSingleInstanceItem ? null : !(mFragmentStack == null || mFragmentStack.isEmpty()) ? mFragmentStack.get(mFragmentStack.size() - 1) : null;
//            if (!hasSingleInstanceItem) {
//                //Add to back stack
//                mFragmentStack.add(fragmentItem);
//            }
//            fragment.onResume();
//            fragment.onSetupActionBar();
//
//            if (!needAnimation) {
//                if (hasSingleInstanceItem) {
//                    //Clear top from single instance item
//                    for (int i = mSingleInstanceFragmentIndex + 1; i < mFragmentStack.size(); i++) {
//                        removeFragmentFromStack(mFragmentStack.get(i));
//                        i--;
//                    }
//                } else {
//                    presentFragmentInternalRemoveOld(removeLast, currentFragmentItem);
//                }
//            } else {
//                fragment.onOpenAnimationStart();
//                final boolean needClearTop = hasSingleInstanceItem;
//                presentFragmentInternalWithAnimation(fragment, animationType, new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        if (needClearTop) {
//                            //Clear top from single instance item
//                            for (int i = mSingleInstanceFragmentIndex + 1; i < mFragmentStack.size(); i++) {
//                                removeFragmentFromStack(mFragmentStack.get(i));
//                                i--;
//                            }
//                        } else {
//                            presentFragmentInternalRemoveOld(removeLast, currentFragmentItem);
//                        }
//                        fragment.onOpenAnimationEnd();
//                        fragment.onBecomeFullyVisible();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                        super.onAnimationCancel(animation);
//                        presentFragmentInternalRemoveOld(removeLast, currentFragmentItem);
//                        fragment.onOpenAnimationEnd();
//                        fragment.onBecomeFullyVisible();
//                    }
//                });
//            }
//            return true;
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private void presentFragmentInternalWithAnimation(BaseFragment fragment, int animationType, Animator.AnimatorListener animatorListener) {
//        String propertyAnimation = animationType == FADE_IN ? "alpha" : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? "translationX" : "";
//        float valueFrom, valueTo;
//        valueFrom = animationType == FADE_IN ? 0f : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? getResources().getDisplayMetrics().widthPixels : -1f;
//        valueTo = animationType == FADE_IN ? 1f : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? 0f : -1f;
//        ArrayList<Animator> animators = new ArrayList<>();
//        if (animationType == TRANSLATION_WITH_FADE_IN) {
//            animators.add(ObjectAnimator.ofFloat(fragment.mFragmentView, propertyAnimation, valueFrom, valueTo));
//            animators.add(ObjectAnimator.ofFloat(fragment.mFragmentView, "alpha", 0f, 1f));
//        } else {
//            animators.add(ObjectAnimator.ofFloat(fragment.mFragmentView, propertyAnimation, valueFrom, valueTo));
//        }
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animators);
//        animatorSet.addListener(animatorListener);
//        animatorSet.setDuration(300);
//        animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
//        animatorSet.start();
//    }
//
//    private void presentFragmentInternalRemoveOld(boolean removeLast, final FragmentItem fragmentItem) {
//        if (fragmentItem == null || fragmentItem.fragment == null) {
//            isShowingFragment = false;
//            return;
//        }
//        fragmentItem.fragment.onPause();
//        if (removeLast) {
//            fragmentItem.fragment.onDestroy();
//            fragmentItem.fragment.setBaseActivity(null);
//            mFragmentStack.remove(fragmentItem);
//        }
//        if (fragmentItem.fragment.mFragmentView != null) {
//            ViewGroup parent = (ViewGroup) fragmentItem.fragment.mFragmentView.getParent();
//            if (parent != null) {
//                parent.removeView(fragmentItem.fragment.mFragmentView);
//            }
//        }
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isShowingFragment = false;
//            }
//        }, 200);
//    }
//
////    public boolean addFragmentToStack(BaseFragment baseFragment, int position) {
////        baseFragment.setBaseActivity(this);
////        if (position == -1) {
////            if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
////                BaseFragment previousFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
////                previousFragment.onPause();
////                if (previousFragment.mFragmentView != null) {
////                    ViewGroup parent = (ViewGroup) previousFragment.mFragmentView.getParent();
////                    if (parent != null) {
////                        parent.removeView(previousFragment.mFragmentView);
////                    }
////                }
////            }
////            mFragmentStack.add(baseFragment);
////        } else {
////            mFragmentStack.add(position, baseFragment);
////        }
////        return true;
////    }
//
//    public void showLastFragment(Bundle savedInstanceState) {
//        try {
//            if (mFragmentStack == null || mFragmentStack.isEmpty()) {
//                return;
//            }
//            boolean needRunOnUIThread = false;
//            FragmentItem currentFragmentItem = mFragmentStack.get(mFragmentStack.size() - 1);
//            BaseFragment previousFragment = currentFragmentItem.getFragment();
//            if (previousFragment == null) {
//                currentFragmentItem.fragment = currentFragmentItem.cls.newInstance();
//                previousFragment = currentFragmentItem.fragment;
//                needRunOnUIThread = true;
//            }
//            previousFragment.setBaseActivity(this);
//            View fragmentView = previousFragment.mFragmentView;
//            if (fragmentView == null) {
//                fragmentView = previousFragment.onCreateView(this, mContainerView);
//            } else {
//                ViewGroup parent = (ViewGroup) fragmentView.getParent();
//                if (parent != null) {
//                    parent.removeView(fragmentView);
//                }
//            }
//            if (savedInstanceState != null) {
//                previousFragment.onRestoreInstanceState(savedInstanceState);
//            }
//            mContainerView.addView(fragmentView);
//            ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            fragmentView.setLayoutParams(layoutParams);
//            previousFragment.onResume();
//            if (needRunOnUIThread) {
//                final BaseFragment currentFragment = previousFragment;
//                MainApplication.getInstance().runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (currentFragment != null)
//                            currentFragment.onSetupActionBar();
//                    }
//                });
//            } else {
//                previousFragment.onSetupActionBar();
//            }
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void removeFragmentFromStack(FragmentItem fragmentItem) {
//        if (fragmentItem == null || fragmentItem.fragment == null)
//            return;
//        fragmentItem.fragment.onPause();
//        fragmentItem.fragment.onDestroy();
//        fragmentItem.fragment.setBaseActivity(null);
//        mFragmentStack.remove(fragmentItem);
//    }
//
//    public void removeAllFragments() {
//        for (int i = 0; i < mFragmentStack.size(); i++) {
//            removeFragmentFromStack(mFragmentStack.get(i));
//            i--;
//        }
//    }
//
//    public boolean popBackStack(boolean animated) {
//        if (mFragmentStack == null || mFragmentStack.isEmpty() || mFragmentStack.size() == 1)
//            return false;
//        if (isPoppingStack) return true;
//        isPoppingStack = true;
//        FragmentItem fragmentItem = mFragmentStack.get(mFragmentStack.size() - 1);
//        BaseFragment currentFragment = fragmentItem.getFragment();
//        fragmentItem.fragment.onPause();
//        View fragmentView = currentFragment.mFragmentView;
//        if (fragmentView != null) {
//            ViewGroup parent = (ViewGroup) fragmentView.getParent();
//            if (parent != null) {
//                parent.removeView(fragmentView);
//            }
//        }
//        //TODO: check single instance before destroy fragment
//        if (!BaseFragment.SingleInstance.class.isInstance(fragmentItem.fragment)) {
//            fragmentItem.fragment.onDestroy();
//        }
//        fragmentItem.fragment.setBaseActivity(null);
//        mFragmentStack.remove(fragmentItem);
//        showLastFragment(mSavedInstanceState);
//        if (currentFragment.mRequestCode >= 0) {
//            BaseFragment lastFragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//            lastFragment.onActivityResult(currentFragment.mRequestCode, currentFragment.mResultCode, currentFragment.mData);
//        }
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isPoppingStack = false;
//            }
//        }, 200);
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
//            BaseFragment fragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//            if (fragment != null && fragment.onBackPressed()) {
//                return;
//            }
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
//            BaseFragment fragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//            if (fragment != null && fragment.onOptionItemSelected(item)) {
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//
//        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
//            BaseFragment fragment = mFragmentStack.get(mFragmentStack.size() - 1).getFragment();
//            if (fragment != null) {
//                fragment.onSaveInstanceState(outState);
//            }
//        }
//        if (outState != null) {
//            outState.putParcelableArrayList(FRAGMENT_STACK_KEY, mFragmentStack);
//        }
//        super.onSaveInstanceState(outState);
//    }
//
//    public enum FragmentType {
//        MAIN(1, MainFragment.class),
//        CHAT(2, ChatFragment.class),
//        PROFILE(3, ProfileFragment.class);
//
//        private int typeId;
//        private Class<? extends BaseFragment> cls;
//
//        FragmentType(int typeId, Class<? extends BaseFragment> cls) {
//            this.typeId = typeId;
//            this.cls = cls;
//        }
//
//        public int getTypeId() {
//            return typeId;
//        }
//
//        public Class<? extends BaseFragment> getFragmentClass() {
//            return cls;
//        }
//
//        public static FragmentType getFragmentTypeById(int typeId) {
//            for (FragmentType typeEnum : FragmentType.values()) {
//                if (typeEnum.getTypeId() == typeId) {
//                    return typeEnum;
//                }
//            }
//            return null;
//        }
//
//        public static FragmentType getFragmentTypeByClass(Class<? extends BaseFragment> cls) {
//            for (FragmentType typeEnum : FragmentType.values()) {
//                if (typeEnum.cls == cls) {
//                    return typeEnum;
//                }
//            }
//            return null;
//        }
//    }
//
//    private static class FragmentItem implements Parcelable {
//        private String tag;
//        private final Class<? extends BaseFragment> cls;
//
//        private BaseFragment fragment;
//        private final Bundle data = new Bundle();
//
//        private String callerFragmentTag;
//        private int requestCode = 0;
//
//        private final FragmentType fragmentType;
//
//        private FragmentItem(Parcel in) {
//            tag = in.readString();
//
//            Object obj = in.readSerializable();
//            if (obj instanceof Class<?>) {
//                cls = (Class<? extends BaseFragment>) obj;
//            } else {
//                cls = null;
//            }
//
//            Bundle dataTemp = in.readBundle(MainApplication.getInstance().getAppContext().getClassLoader());
//            if (dataTemp != null) data.putAll(dataTemp);
//
//            fragment = null;
//            callerFragmentTag = in.readString();
//            requestCode = in.readInt();
//
//            fragmentType = (FragmentType) in.readSerializable();
//        }
//
//        public FragmentItem(FragmentType fragmentType, Bundle data) {
//            this.cls = fragmentType.getFragmentClass();
//            tag = UUID.randomUUID().toString();
//            if (data != null) this.data.putAll(data);
//            this.fragmentType = fragmentType;
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(tag);
//            dest.writeSerializable(cls);
//            dest.writeBundle(data);
//            dest.writeString(callerFragmentTag);
//            dest.writeInt(requestCode);
//            dest.writeSerializable(fragmentType);
//        }
//
//        public static final Parcelable.Creator<FragmentItem> CREATOR
//                = new Parcelable.Creator<FragmentItem>() {
//            public FragmentItem createFromParcel(Parcel in) {
//                return new FragmentItem(in);
//            }
//
//            public FragmentItem[] newArray(int size) {
//                return new FragmentItem[size];
//            }
//        };
//
//        public BaseFragment getFragment() {
//            return fragment;
//        }
//    }

}
