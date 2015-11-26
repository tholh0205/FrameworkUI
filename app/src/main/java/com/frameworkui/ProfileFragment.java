package com.frameworkui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.frameworkui.uicore.actionbar.ActionBar;
import com.frameworkui.uicore.actionbar.ActionBarMenu;
import com.frameworkui.uicore.actionbar.ActionBarMenuItem;
import com.frameworkui.uicore.actionbar.MenuDrawable;
import com.frameworkui.uicore.BaseFragment;
import com.frameworkui.uicore.FragmentData;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ProfileFragment extends BaseFragment {

    private ActionBarMenu mMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hasMenu = true;
        mFragmentView = inflater.inflate(R.layout.profile_fragment, container, false);
        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getBaseActivity().presentFragment(BaseActivity.FragmentType.CHAT, null, 2222, BaseActivity.TRANSLATION_WITH_FADE_IN);
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.GALLERY, null, 0, true, false);
            }
        });
        mFragmentView.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getBaseActivity().presentFragment(BaseActivity.FragmentType.CHAT, null, 2222, BaseActivity.TRANSLATION_WITH_FADE_IN);
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.SEARCH, null, 0, false, false);
            }
        });
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().showFragment((FrameLayout) getView().findViewById(R.id.child_fragment_container), new ChildFragment());
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
//        ActionBar actionBar = getActivity().getSupportActionBar();
//        if (actionBar != null) {
//            setTitle("Profile Fragment");
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        ActionBar actionBar = (ActionBar) getView().findViewById(R.id.custom_action_bar);
        if (actionBar != null) {
            actionBar.setTitle("Profile Fragment");
            actionBar.setBackButtonDrawable(getUpIndicator());
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        Intent data = new Intent();
//        data.putExtra("ThoLH", "Testing");
//        setResult(Activity.RESULT_OK, data);
//        return super.onBackPressed();
//    }


    @Override
    public void onCreateOptionsMenu(ActionBarMenu menu) {
        ActionBarMenuItem itemMore = menu.addItem(1, new MenuDrawable());
        itemMore.addSubItem(2, "Test 1", 0);
        itemMore.addSubItem(3, "Test 2", 0);
        itemMore.addSubItem(4, "Test 3", 0);
        itemMore.addSubItem(5, "Test 4", 0);
        itemMore.addSubItem(6, "Test 5", 0);
        itemMore.addSubItem(7, "Test 6", 0);
    }

    @Override
    public void onResume() {
        android.util.Log.d("ThoLH", "ProfileFragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("ThoLH", "ProfileFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("ThoLH", "ProfileFragment onDestroy");
    }

}
