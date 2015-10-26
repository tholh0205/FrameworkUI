package com.frameworkui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/26/15.
 */
public class GalleryFragment extends BaseFragment implements BaseFragment.KeepBelowFragment {

    @Override
    public View onCreateView(Context context, ViewGroup container) {
        mFragmentView = LayoutInflater.from(context).inflate(R.layout.gallery_fragment, container, false);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d("ThoLH", "Gallery Fragment onClick");
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        finishFragment(false);
        return true;
    }
}
