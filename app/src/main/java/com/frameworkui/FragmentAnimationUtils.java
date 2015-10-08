package com.frameworkui;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

/**
 * Created by ThoLH on 10/7/15.
 */
public class FragmentAnimationUtils {

    private static AnimatorSet sCurrentAnimatorSet;

    public static void cancelCurrentAnimation() {
        if (sCurrentAnimatorSet != null && sCurrentAnimatorSet.isRunning()) {
            sCurrentAnimatorSet.cancel();
            sCurrentAnimatorSet = null;
        }
    }

    public static boolean isRunning() {
        if (sCurrentAnimatorSet != null) {
            return sCurrentAnimatorSet.isRunning();
        }
        return false;
    }

    public static void openTranslationWithFadeIn(View topView, Animator.AnimatorListener animatorListener) {
        if (topView == null)
            return;
        cancelCurrentAnimation();
        int screenWidth = topView.getResources().getDisplayMetrics().widthPixels;
        ArrayList<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(topView, "translationX", screenWidth / 2f, 0f));
        animators.add(ObjectAnimator.ofFloat(topView, "alpha", 0f, 1f));
        sCurrentAnimatorSet = new AnimatorSet();
        sCurrentAnimatorSet.playTogether(animators);
        sCurrentAnimatorSet.addListener(animatorListener);
        sCurrentAnimatorSet.setDuration(300L);
        sCurrentAnimatorSet.setInterpolator(new DecelerateInterpolator(1.5F));
        sCurrentAnimatorSet.start();
    }

    public static void closeTranslationWithFadeIn(View topView, Animator.AnimatorListener animatorListener) {
        if (topView == null)
            return;
        cancelCurrentAnimation();
        int screenWidth = topView.getResources().getDisplayMetrics().widthPixels;
        ArrayList<Animator> animators = new ArrayList<>();
        animators.add(ObjectAnimator.ofFloat(topView, "translationX", 0f, screenWidth / 2f));
        animators.add(ObjectAnimator.ofFloat(topView, "alpha", 1f, 0f));
        sCurrentAnimatorSet = new AnimatorSet();
        sCurrentAnimatorSet.playTogether(animators);
        sCurrentAnimatorSet.addListener(animatorListener);
        sCurrentAnimatorSet.setDuration(300L);
        sCurrentAnimatorSet.setInterpolator(new DecelerateInterpolator(1.5F));
        sCurrentAnimatorSet.start();
    }

}
