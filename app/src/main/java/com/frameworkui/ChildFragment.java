package com.frameworkui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/26/15.
 */
public class ChildFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            android.util.Log.d("ThoLH", "onCreate ChildFragment = " + savedInstanceState.getString("ChildFragment"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.child_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getChildFragmentManager().remove(ChildFragment.this);
            }
        });

    }

    @Override
    public void onResume() {
        android.util.Log.d("ThoLH", "ChildFragment onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        android.util.Log.d("ThoLH", "ChildFragment onPause");
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.util.Log.d("ThoLH", "ChildFragment OnDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d("ThoLH", "ChildFragment onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ChildFragment", "Zalo");
    }
}
