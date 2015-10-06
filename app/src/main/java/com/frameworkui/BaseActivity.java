package com.frameworkui;

import android.os.Bundle;
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
    public static final int NO_ANIMATION = -1, FADE_IN = 0, TRANSLATION = 1, TRANSLATION_WITH_FADE_IN = 2;

    private ViewGroup mContainerView;
    private ArrayList<BaseFragment> mFragmentStack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        setContentView(R.layout.activity_main);
        mContainerView = (ViewGroup) findViewById(R.id.fragment_container);
        mFragmentStack = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
            mFragmentStack.get(mFragmentStack.size() - 1).onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
            mFragmentStack.get(mFragmentStack.size() - 1).onPause();
        }
    }

    public boolean presentFragment(final BaseFragment fragment, final boolean removeLast, int animationType) {
        boolean needAnimation = animationType != NO_ANIMATION && getResources().getBoolean(R.bool.use_view_animation);
        final BaseFragment currentFragment = !(mFragmentStack == null || mFragmentStack.isEmpty()) ? mFragmentStack.get(mFragmentStack.size() - 1) : null;
        fragment.setBaseActivity(this);
        View fragmentView = fragment.mFragmentView;
        if (fragmentView == null) {
            fragmentView = fragment.onCreateView(this, mContainerView);
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        mContainerView.addView(fragmentView);
        ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        fragmentView.setLayoutParams(layoutParams);
        mFragmentStack.add(fragment);
        fragment.onResume();
        fragmentView.bringToFront();
        fragment.onSetupActionBar();

        if (!needAnimation) {
            presentFragmentInternalRemoveOld(removeLast, currentFragment);
        } else {
            fragment.onOpenAnimationStart();
            presentFragmentInternalWithAnimation(animationType, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    presentFragmentInternalRemoveOld(removeLast, currentFragment);
                    fragment.onOpenAnimationEnd();
                    fragment.onBecomeFullyVisible();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    presentFragmentInternalRemoveOld(removeLast, currentFragment);
                    fragment.onOpenAnimationEnd();
                    fragment.onBecomeFullyVisible();
                }
            });
        }
        return true;
    }

    private void presentFragmentInternalWithAnimation(int animationType, Animator.AnimatorListener animatorListener) {
        BaseFragment currentFragment = mFragmentStack.get(mFragmentStack.size() - 1);
        String propertyAnimation = animationType == FADE_IN ? "alpha" : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? "translationX" : "";
        float valueFrom, valueTo;
        valueFrom = animationType == FADE_IN ? 0f : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? getResources().getDisplayMetrics().widthPixels / 2 : -1f;
        valueTo = animationType == FADE_IN ? 1f : animationType == TRANSLATION || animationType == TRANSLATION_WITH_FADE_IN ? 0f : -1f;
        ArrayList<Animator> animators = new ArrayList<>();
        if (animationType == TRANSLATION_WITH_FADE_IN) {
            animators.add(ObjectAnimator.ofFloat(currentFragment.mFragmentView, propertyAnimation, valueFrom, valueTo));
            animators.add(ObjectAnimator.ofFloat(currentFragment.mFragmentView, "alpha", 0f, 1f));
        } else {
            animators.add(ObjectAnimator.ofFloat(currentFragment.mFragmentView, propertyAnimation, valueFrom, valueTo));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.addListener(animatorListener);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
        animatorSet.start();
    }

    private void presentFragmentInternalRemoveOld(boolean removeLast, final BaseFragment fragment) {
        if (fragment == null) {
            return;
        }
        fragment.onPause();
        if (removeLast) {
            fragment.onDestroy();
            fragment.setBaseActivity(null);
            mFragmentStack.remove(fragment);
        }
        if (fragment.mFragmentView != null) {
            ViewGroup parent = (ViewGroup) fragment.mFragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragment.mFragmentView);
            }
        }
    }

    public boolean addFragmentToStack(BaseFragment baseFragment, int position) {
        baseFragment.setBaseActivity(this);
        if (position == -1) {
            if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
                BaseFragment previousFragment = mFragmentStack.get(mFragmentStack.size() - 1);
                previousFragment.onPause();
                if (previousFragment.mFragmentView != null) {
                    ViewGroup parent = (ViewGroup) previousFragment.mFragmentView.getParent();
                    if (parent != null) {
                        parent.removeView(previousFragment.mFragmentView);
                    }
                }
            }
            mFragmentStack.add(baseFragment);
        } else {
            mFragmentStack.add(position, baseFragment);
        }
        return true;
    }

    public void showLastFragment() {
        if (mFragmentStack == null || mFragmentStack.isEmpty()) {
            return;
        }
        BaseFragment previousFragment = mFragmentStack.get(mFragmentStack.size() - 1);
        previousFragment.setBaseActivity(this);
        View fragmentView = previousFragment.mFragmentView;
        if (fragmentView == null) {
            fragmentView = previousFragment.onCreateView(this, mContainerView);
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        mContainerView.addView(fragmentView);
        ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        fragmentView.setLayoutParams(layoutParams);
        previousFragment.onResume();
        previousFragment.onSetupActionBar();
    }

    public void removeFragmentFromStack(BaseFragment fragment) {
        fragment.onPause();
        fragment.onDestroy();
        fragment.setBaseActivity(null);
        mFragmentStack.remove(fragment);
    }

    public void removeAllFragments() {
        for (int i = 0; i < mFragmentStack.size(); i++) {
            removeFragmentFromStack(mFragmentStack.get(i));
            i--;
        }
    }

    public boolean popBackStack(boolean animated) {
        if (mFragmentStack == null || mFragmentStack.isEmpty() || mFragmentStack.size() == 1)
            return false;
        BaseFragment currentFragment = mFragmentStack.get(mFragmentStack.size() - 1);
        removeFragmentFromStack(currentFragment);
        View fragmentView = currentFragment.mFragmentView;
        if (fragmentView != null) {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        showLastFragment();
        if (currentFragment.mRequestCode >= 0) {
            BaseFragment lastFragment = mFragmentStack.get(mFragmentStack.size() - 1);
            lastFragment.onActivityResultFragment(currentFragment.mRequestCode, currentFragment.mResultCode, currentFragment.mData);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mFragmentStack != null && !mFragmentStack.isEmpty()) {
            if (mFragmentStack.get(mFragmentStack.size() - 1).onOptionItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        android.util.Log.d("ThoLH", "BaseActivity onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        android.util.Log.d("ThoLH", "BaseActivity onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    public enum FragmentType {
        MAIN(1, MainFragment.class),
        CHAT(2, ChatFragment.class);

        private int typeId;
        private Class<? extends BaseFragment> cls;

        FragmentType(int typeId, Class<? extends BaseFragment> cls) {
            this.typeId = typeId;
            this.cls = cls;
        }

        public int getTypeId() {
            return typeId;
        }

        public Class<? extends BaseFragment> getFragmentClass() {
            return cls;
        }

        public static FragmentType getFragmentTypeById(int typeId) {
            for (FragmentType typeEnum : FragmentType.values()) {
                if (typeEnum.getTypeId() == typeId) {
                    return typeEnum;
                }
            }
            return null;
        }

        public static FragmentType getFragmentTypeByClass(Class<? extends BaseFragment> cls) {
            for (FragmentType typeEnum : FragmentType.values()) {
                if (typeEnum.cls == cls) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    private static class FragmentItem implements Parcelable {
        private String tag;
        private final Class<? extends BaseFragment> cls;

        private BaseFragment fragment;
        private final Bundle data = new Bundle();

        private String callerFragmentTag;
        private int requestCode = 0;

        private final FragmentType fragmentType;

        private FragmentItem(Parcel in) {
            tag = in.readString();

            Object obj = in.readSerializable();
            if (obj instanceof Class<?>) {
                cls = (Class<? extends BaseFragment>) obj;
            } else {
                cls = null;
            }

            Bundle dataTemp = in.readBundle(MainApplication.getInstance().getAppContext().getClassLoader());
            if (dataTemp != null) data.putAll(dataTemp);

            fragment = null;
            callerFragmentTag = in.readString();
            requestCode = in.readInt();

            fragmentType = (FragmentType) in.readSerializable();
        }

        public FragmentItem(FragmentType fragmentType, Bundle data) {
            this.cls = fragmentType.getFragmentClass();
            tag = UUID.randomUUID().toString();
            if (data != null) this.data.putAll(data);
            this.fragmentType = fragmentType;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(tag);
            dest.writeSerializable(cls);
            dest.writeBundle(data);
            dest.writeString(callerFragmentTag);
            dest.writeInt(requestCode);
            dest.writeSerializable(fragmentType);
        }

        public static final Parcelable.Creator<FragmentItem> CREATOR
                = new Parcelable.Creator<FragmentItem>() {
            public FragmentItem createFromParcel(Parcel in) {
                return new FragmentItem(in);
            }

            public FragmentItem[] newArray(int size) {
                return new FragmentItem[size];
            }
        };

        public BaseFragment getFragment() {
            return fragment;
        }
    }

}
