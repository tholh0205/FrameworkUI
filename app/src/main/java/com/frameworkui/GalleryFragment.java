package com.frameworkui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/26/15.
 */
public class GalleryFragment extends BaseFragment implements BaseFragment.KeepBelowFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.gallery_fragment, container, false);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d("ThoLH", "Gallery Fragment onClick");
            }
        });
    }

    @Override
    public boolean finishFragment(boolean animated) {
        return super.finishFragment(false);
    }
}
