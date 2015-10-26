package com.frameworkui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class MainFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            android.util.Log.d("ThoLH", "savedInstanceState = " + savedInstanceState + "  MainFragment = " + savedInstanceState.getString("MainFragment"));
    }

    @Override
    public View onCreateView(Context context, ViewGroup container) {
        mFragmentView = LayoutInflater.from(context).inflate(R.layout.main_fragment, container, false);
        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, 1111, BaseActivity.TRANSLATION_WITH_FADE_IN);
                Bundle bundle = new Bundle();
                bundle.putString("MainFragment", "Zalo");
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.CHAT, bundle, 1111, false, false);
            }
        });
        return mFragmentView;
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
        ActionBar actionBar = getActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Main Fragment");
        }
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
        android.util.Log.d("ThoLH", "MainFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d("ThoLH", "MainFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("ThoLH", "MainFragment onDestroy");
    }

    @Override
    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        super.onActivityResultFragment(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "Chat Fragment " + data.getExtras(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MainFragment", "ThoLH1");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.util.Log.d("ThoLH", "MainFragment onDetach");
    }
}
