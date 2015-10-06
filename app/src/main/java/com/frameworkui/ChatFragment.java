package com.frameworkui;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ChatFragment extends BaseFragment {
    @Override
    public View onCreateView(Context context, ViewGroup container) {
        mFragmentView = LayoutInflater.from(context).inflate(R.layout.chat_fragment, container, false);
        return mFragmentView;
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
        ActionBar actionBar = getBaseActivity().getSupportActionBar();
        if(actionBar != null) {
            setTitle("Chat Fragment");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
