package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ChatFragment extends BaseFragment implements BaseFragment.SingleInstance {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("ThoLH", "ChatFragment savedInstanceState = " + savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasMenu(true);
        mFragmentView = inflater.inflate(R.layout.chat_fragment, container, false);
        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, -1, BaseActivity.TRANSLATION_WITH_FADE_IN);
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.PROFILE, null, 0, false, false);
            }
        });
        android.util.Log.d("ThoLH", " ChatFragment get key MainFragment = " + getArguments().getString("MainFragment"));
        return mFragmentView;
    }

    @Override
    public void onNewIntent() {
        super.onNewIntent();
        android.util.Log.d("ThoLH", "ChatFragment onNewIntent " + getArguments());
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
        ActionBar actionBar = getActivity().getSupportActionBar();
        if (actionBar != null) {
            setTitle("Chat Fragment");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.chat_menu, menu);
        MenuItem itemMore = menu.findItem(R.id.menu_more);
        itemMore.setIcon(getMenuMoreDrawable());
    }

    @Override
    public boolean onBackPressed() {
        Intent data = new Intent();
        data.putExtra("ThoLH", "Testing");
        setResult(Activity.RESULT_OK, data);
        return super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        android.util.Log.d("ThoLH", "ChatFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("ThoLH", "ChatFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("ThoLH", "ChatFragment onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
