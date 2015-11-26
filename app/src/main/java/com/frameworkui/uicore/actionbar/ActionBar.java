package com.frameworkui.uicore.actionbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frameworkui.uicore.BaseFragment;
import com.frameworkui.MainApplication;
import com.frameworkui.R;
import com.frameworkui.uicore.Utils;

/**
 * Created by ThoLH on 11/17/15.
 */
public class ActionBar extends FrameLayout {

    public static class ActionBarMenuOnItemClick {
        public void onItemClick(int id) {
        }

        public boolean canOpenMenu() {
            return true;
        }
    }

    private ImageView backButtonImageView;
    private TextView titleTextView;
    private TextView subtitleTextView;
    private View actionModeTop;
    private ActionBarMenu menu;
    private ActionBarMenu actionMode;
    private boolean occupyStatusBar = MainApplication.getInstance().getAppContext().getResources().getBoolean(R.bool.use_translucent_status);

    private boolean allowOverlayTitle;
    private CharSequence lastTitle;
    private boolean castShadows = true;
    protected boolean isSearchFieldVisible;
    protected int itemsBackgroundResourceId;
    private boolean isBackOverlayVisible;
    public BaseFragment parentFragment;
    public ActionBarMenuOnItemClick actionBarMenuOnItemClick;
    private int extraHeight;

    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void createBackButtonImage() {
        if (backButtonImageView != null) {
            return;
        }
        backButtonImageView = new ImageView(getContext());
        backButtonImageView.setScaleType(ImageView.ScaleType.CENTER);
        backButtonImageView.setBackgroundResource(itemsBackgroundResourceId);
        LayoutParams layoutParams = new LayoutParams(Utils.dp(54), Utils.dp(54));
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        addView(backButtonImageView, layoutParams);

        backButtonImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchFieldVisible) {
                    closeSearchField();
                    return;
                }
                if (actionBarMenuOnItemClick != null) {
                    actionBarMenuOnItemClick.onItemClick(-1);
                }
            }
        });
    }

    public void setBackButtonDrawable(Drawable drawable) {
        if (backButtonImageView == null) {
            createBackButtonImage();
        }
        backButtonImageView.setVisibility(drawable == null ? GONE : VISIBLE);
        backButtonImageView.setImageDrawable(drawable);
    }

    public void setBackButtonImage(int resourceId) {
        if (backButtonImageView == null) {
            createBackButtonImage();
        }
        backButtonImageView.setVisibility(resourceId == 0 ? GONE : VISIBLE);
        backButtonImageView.setImageResource(resourceId);
    }

    private void createSubtitleTextView() {
        if (subtitleTextView != null) {
            return;
        }
        subtitleTextView = new TextView(getContext());
        subtitleTextView.setGravity(Gravity.LEFT);
        subtitleTextView.setTextColor(0xffd7e8f7);
        subtitleTextView.setSingleLine(true);
        subtitleTextView.setLines(1);
        subtitleTextView.setMaxLines(1);
        subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        subtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
//        subtitleTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf"));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        addView(subtitleTextView, layoutParams);
    }

    public void setSubtitle(CharSequence value) {
        if (value != null && subtitleTextView == null) {
            createSubtitleTextView();
        }
        if (subtitleTextView != null) {
            subtitleTextView.setVisibility(value != null && !isSearchFieldVisible ? VISIBLE : INVISIBLE);
            subtitleTextView.setText(value);
        }
    }

    private void createTitleTextView() {
        if (titleTextView != null) {
            return;
        }
        titleTextView = new TextView(getContext());
        titleTextView.setGravity(Gravity.LEFT);
        titleTextView.setTextColor(0xffffffff);
        titleTextView.setSingleLine(true);
        titleTextView.setLines(1);
        titleTextView.setMaxLines(1);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        titleTextView.setEllipsize(TextUtils.TruncateAt.END);
//        titleTextView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/rmedium.ttf"));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        addView(titleTextView, layoutParams);
    }

    public void setTitle(CharSequence value) {
        if (value != null && titleTextView == null) {
            createTitleTextView();
        }
        if (titleTextView != null) {
            titleTextView.setVisibility(value != null && !isSearchFieldVisible ? VISIBLE : INVISIBLE);
            titleTextView.setText(value);
        }
    }

    public TextView getsubtitleTextView() {
        return subtitleTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public Drawable getSubtitleIcon() {
        return subtitleTextView.getCompoundDrawables()[0];
    }

    public String getTitle() {
        if (titleTextView == null) {
            return null;
        }
        return titleTextView.getText().toString();
    }

    public ActionBarMenu createMenu() {
        if (menu != null) {
            return menu;
        }
        menu = new ActionBarMenu(getContext(), this);
        addView(menu);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) menu.getLayoutParams();
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.RIGHT;
        menu.setLayoutParams(layoutParams);
        return menu;
    }

    public void setActionBarMenuOnItemClick(ActionBarMenuOnItemClick listener) {
        actionBarMenuOnItemClick = listener;
    }

    public ActionBarMenu createActionMode() {
        if (actionMode != null) {
            return actionMode;
        }
        actionMode = new ActionBarMenu(getContext(), this);
        actionMode.setBackgroundResource(R.drawable.editheader);
        addView(actionMode);
        actionMode.setPadding(0, occupyStatusBar ? Utils.sStatusBarHeight : 0, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionMode.getLayoutParams();
        layoutParams.height = LayoutParams.MATCH_PARENT;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.RIGHT;
        actionMode.setLayoutParams(layoutParams);
        actionMode.setVisibility(INVISIBLE);

        if (occupyStatusBar && actionModeTop == null) {
            actionModeTop = new View(getContext());
            actionModeTop.setBackgroundColor(0x99000000);
            addView(actionModeTop);
            layoutParams = (LayoutParams) actionModeTop.getLayoutParams();
            layoutParams.height = Utils.sStatusBarHeight;
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            actionModeTop.setLayoutParams(layoutParams);
            actionModeTop.setVisibility(INVISIBLE);
        }
        return actionMode;
    }

    public void showActionMode() {
        if (actionMode == null) {
            return;
        }
        actionMode.setVisibility(VISIBLE);
        if (occupyStatusBar && actionModeTop != null) {
            actionModeTop.setVisibility(VISIBLE);
        }
        if (titleTextView != null) {
            titleTextView.setVisibility(INVISIBLE);
        }
        if (subtitleTextView != null) {
            subtitleTextView.setVisibility(INVISIBLE);
        }
        if (backButtonImageView != null) {
            backButtonImageView.setVisibility(INVISIBLE);
        }
        if (menu != null) {
            menu.setVisibility(INVISIBLE);
        }
    }

    public void hideActionMode() {
        if (actionMode == null) {
            return;
        }
        actionMode.setVisibility(INVISIBLE);
        if (occupyStatusBar && actionModeTop != null) {
            actionModeTop.setVisibility(INVISIBLE);
        }
        if (titleTextView != null) {
            titleTextView.setVisibility(VISIBLE);
        }
        if (subtitleTextView != null) {
            subtitleTextView.setVisibility(VISIBLE);
        }
        if (backButtonImageView != null) {
            backButtonImageView.setVisibility(VISIBLE);
        }
        if (menu != null) {
            menu.setVisibility(VISIBLE);
        }
    }

    public void showActionModeTop() {
        if (occupyStatusBar && actionModeTop == null) {
            actionModeTop = new View(getContext());
            actionModeTop.setBackgroundColor(0x99000000);
            addView(actionModeTop);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) actionModeTop.getLayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.height = Utils.sStatusBarHeight;
            layoutParams.width = LayoutParams.MATCH_PARENT;
            actionModeTop.setLayoutParams(layoutParams);
        }
    }

    public boolean isActionModeShowed() {
        return actionMode != null && actionMode.getVisibility() == VISIBLE;
    }

    protected void onSearchFieldVisibilityChanged(boolean visible) {
        isSearchFieldVisible = visible;
        if (titleTextView != null) {
            titleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }
        if (subtitleTextView != null) {
            subtitleTextView.setVisibility(visible ? INVISIBLE : VISIBLE);
        }
        Drawable drawable = backButtonImageView.getDrawable();
        if (drawable != null && drawable instanceof MenuDrawable) {
            ((MenuDrawable) drawable).setRotation(visible ? 1 : 0, true);
        }
    }

    public void closeSearchField() {
        if (!isSearchFieldVisible || menu == null) {
            return;
        }
        menu.closeSearchField();
    }

    public void openSearchField(String text) {
        if (menu == null || text == null) {
            return;
        }
        menu.openSearchField(!isSearchFieldVisible, text);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int actionBarHeight = getCurrentActionBarHeight();
        int actionBarHeightSpec = MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.EXACTLY);

        setMeasuredDimension(width, actionBarHeight + extraHeight + (occupyStatusBar ? Utils.sStatusBarHeight : 0));

        int textLeft;
        if (backButtonImageView != null && backButtonImageView.getVisibility() != GONE) {
            backButtonImageView.measure(MeasureSpec.makeMeasureSpec(Utils.dp(54), MeasureSpec.EXACTLY), actionBarHeightSpec);
            textLeft = Utils.dp(Utils.isTablet() ? 80 : 72);
        } else {
            textLeft = Utils.dp(Utils.isTablet() ? 26 : 18);
        }

        if (menu != null && menu.getVisibility() != GONE) {
            int menuWidth;
            if (isSearchFieldVisible) {
                menuWidth = MeasureSpec.makeMeasureSpec(width - Utils.dp(Utils.isTablet() ? 74 : 66), MeasureSpec.EXACTLY);
            } else {
                menuWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            }
            menu.measure(menuWidth, actionBarHeightSpec);
        }

        if (titleTextView != null && titleTextView.getVisibility() != GONE || subtitleTextView != null && subtitleTextView.getVisibility() != GONE) {
            int availableWidth = width - (menu != null ? menu.getMeasuredWidth() : 0) - Utils.dp(16) - textLeft;

            if (titleTextView != null && titleTextView.getVisibility() != GONE) {
                titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, !Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 18 : 20);
                titleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.AT_MOST));

            }
            if (subtitleTextView != null && subtitleTextView.getVisibility() != GONE) {
                subtitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, !Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 14 : 16);
                subtitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(actionBarHeight, MeasureSpec.AT_MOST));
            }
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE || child == titleTextView || child == subtitleTextView || child == menu || child == backButtonImageView) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY), 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int additionalTop = occupyStatusBar ? Utils.sStatusBarHeight : 0;

        int textLeft;
        if (backButtonImageView != null && backButtonImageView.getVisibility() != GONE) {
            backButtonImageView.layout(0, additionalTop, backButtonImageView.getMeasuredWidth(), additionalTop + backButtonImageView.getMeasuredHeight());
            textLeft = Utils.dp(Utils.isTablet() ? 80 : 72);
        } else {
            textLeft = Utils.dp(Utils.isTablet() ? 26 : 18);
        }

        if (menu != null && menu.getVisibility() != GONE) {
            int menuLeft = isSearchFieldVisible ? Utils.dp(Utils.isTablet() ? 74 : 66) : (right - left) - menu.getMeasuredWidth();
            menu.layout(menuLeft, additionalTop, menuLeft + menu.getMeasuredWidth(), additionalTop + menu.getMeasuredHeight());
        }

        int offset = Utils.dp(!Utils.isTablet() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 1 : 2);
        if (titleTextView != null && titleTextView.getVisibility() != GONE) {
            int textTop;
            if (subtitleTextView != null && subtitleTextView.getVisibility() != GONE) {
                textTop = (getCurrentActionBarHeight() / 2 - titleTextView.getMeasuredHeight()) / 2 + offset;
            } else {
                textTop = (getCurrentActionBarHeight() - titleTextView.getMeasuredHeight()) / 2 - Utils.dp(1);
            }
            titleTextView.layout(textLeft, additionalTop + textTop, textLeft + titleTextView.getMeasuredWidth(), additionalTop + textTop + titleTextView.getMeasuredHeight());
        }
        if (subtitleTextView != null && subtitleTextView.getVisibility() != GONE) {
            int textTop = getCurrentActionBarHeight() / 2 + (getCurrentActionBarHeight() / 2 - subtitleTextView.getMeasuredHeight()) / 2 - offset;
            subtitleTextView.layout(textLeft, additionalTop + textTop, textLeft + subtitleTextView.getMeasuredWidth(), additionalTop + textTop + subtitleTextView.getMeasuredHeight());
        }

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE || child == titleTextView || child == subtitleTextView || child == menu || child == backButtonImageView) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int childLeft;
            int childTop;

            int gravity = lp.gravity;
            if (gravity == -1) {
                gravity = Gravity.TOP | Gravity.LEFT;
            }

            final int absoluteGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

            switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    childLeft = (right - left - width) / 2 + lp.leftMargin - lp.rightMargin;
                    break;
                case Gravity.RIGHT:
                    childLeft = right - width - lp.rightMargin;
                    break;
                case Gravity.LEFT:
                default:
                    childLeft = lp.leftMargin;
            }

            switch (verticalGravity) {
                case Gravity.TOP:
                    childTop = lp.topMargin;
                    break;
                case Gravity.CENTER_VERTICAL:
                    childTop = (bottom - top - height) / 2 + lp.topMargin - lp.bottomMargin;
                    break;
                case Gravity.BOTTOM:
                    childTop = (bottom - top) - height - lp.bottomMargin;
                    break;
                default:
                    childTop = lp.topMargin;
            }
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
        }
    }

    public void onMenuButtonPressed() {
        if (menu != null) {
            menu.onMenuButtonPressed();
        }
    }

    protected void onPause() {
        if (menu != null) {
            menu.hideAllPopupMenus();
        }
    }

    public void setAllowOverlayTitle(boolean value) {
        allowOverlayTitle = value;
    }

    public void setTitleOverlayText(String text) {
        if (!allowOverlayTitle /*|| parentFragment.parentLayout == null*/) {
            return;
        }
        CharSequence textToSet = text != null ? text : lastTitle;
        if (textToSet != null && titleTextView == null) {
            createTitleTextView();
        }
        if (titleTextView != null) {
            titleTextView.setVisibility(textToSet != null && !isSearchFieldVisible ? VISIBLE : INVISIBLE);
            titleTextView.setText(textToSet);
        }
    }

    public boolean isSearchFieldVisible() {
        return isSearchFieldVisible;
    }

    public void setExtraHeight(int value, boolean layout) {
        extraHeight = value;
        if (layout) {
            requestLayout();
        }
    }

    public int getExtraHeight() {
        return extraHeight;
    }

    public void setOccupyStatusBar(boolean value) {
        occupyStatusBar = value;
        if (actionMode != null) {
            actionMode.setPadding(0, occupyStatusBar ? Utils.sStatusBarHeight : 0, 0, 0);
        }
    }

    public boolean getOccupyStatusBar() {
        return occupyStatusBar;
    }

    public void setItemsBackground(int resourceId) {
        itemsBackgroundResourceId = resourceId;
        if (backButtonImageView != null) {
            backButtonImageView.setBackgroundResource(itemsBackgroundResourceId);
        }
    }

    public void setCastShadows(boolean value) {
        castShadows = value;
    }

    public boolean getCastShadows() {
        return castShadows;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    public static int getCurrentActionBarHeight() {
        //TODO: check tablet first
        if (Utils.isTablet()) {
            return Utils.dp(64);
        } else if (MainApplication.getInstance().getAppContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return Utils.dp(48);
        } else {
            return Utils.dp(54);
        }
    }

}
