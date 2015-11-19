package com.frameworkui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frameworkui.actionbar.ActionBar;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class MainFragment extends BaseFragment implements BaseFragment.SingleInstance {

    private ViewPager mViewPager;

    @Override
    public boolean isEnableSwipeBack() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.main_fragment, container, false);
//        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, 1111, BaseActivity.TRANSLATION_WITH_FADE_IN);
//                Bundle bundle = new Bundle();
//                bundle.putString("MainFragment", "Zalo");
//                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.CHAT, bundle, 1111, false, false);
//            }
//        });
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.tabs);
        mViewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("CurrentPosition")) {
                mViewPager.setCurrentItem(savedInstanceState.getInt("CurrentPosition"));
            }
        }
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onSetupActionBar() {
        ActionBar actionBar = (ActionBar) getView().findViewById(R.id.custom_action_bar);
        if (actionBar != null) {
            actionBar.setTitle("Main Fragment");
            actionBar.setBackButtonDrawable(getUpIndicator());
            actionBar.setSubtitle("Test Fragment");
        }
//        super.onSetupActionBar();
//        ActionBar actionBar = getActivity().getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle("Main Fragment");
//        }
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "Chat Fragment " + data.getExtras(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentPosition", mViewPager.getCurrentItem());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mViewPager != null)
            mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        if (mViewPager != null)
            mViewPager.setVisibility(View.GONE);
        super.onDetach();
    }

    private class TabsAdapter extends BaseFragmentPagerAdapter {

        private BaseFragment[] fragments = new BaseFragment[4];
        private int mPreviousPosition = 0;

        public TabsAdapter(ChildFragmentManager childFragmentManager) {
            super(childFragmentManager);
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment f = fragments[position];
            if (f == null) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                f = new TabFragment();
                f.setArguments(bundle);
                fragments[position] = f;
            }
            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

    }
}
