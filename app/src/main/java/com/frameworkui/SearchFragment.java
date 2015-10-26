package com.frameworkui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/26/15.
 */
public class SearchFragment extends BaseFragment {

    @Override
    public View onCreateView(Context context, ViewGroup container) {
        return LayoutInflater.from(context).inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view) {
        super.onViewCreated(view);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.CHAT, null, 0, false, false);
            }
        });
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
        setTitle("SearchFragment");
    }
}
