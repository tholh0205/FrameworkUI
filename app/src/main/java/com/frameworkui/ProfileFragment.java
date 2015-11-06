package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ProfileFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        ActionBar actionBar = getActivity().getSupportActionBar();
        if (actionBar != null) {
            setTitle("Profile Fragment");
            actionBar.setDisplayHomeAsUpEnabled(true);
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
