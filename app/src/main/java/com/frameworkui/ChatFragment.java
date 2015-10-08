package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ChatFragment extends BaseFragment implements BaseFragment.SingleInstance {
    @Override
    public View onCreateView(Context context, ViewGroup container) {
        mFragmentView = LayoutInflater.from(context).inflate(R.layout.chat_fragment, container, false);
        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, -1, BaseActivity.TRANSLATION_WITH_FADE_IN);
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.PROFILE, null, 0, false, false);
            }
        });
        return mFragmentView;
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

}
