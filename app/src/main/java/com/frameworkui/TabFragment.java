package com.frameworkui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ThoLH on 10/26/15.
 */
public class TabFragment extends BaseFragment implements BaseFragment.SingleInstance{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("MainFragment", "Zalo");
                ((BaseFragmentActivity) getActivity()).getFragmentManagerLayout().showFragment(FragmentData.FragmentType.CHAT, bundle, 1111, false, false);
            }
        });
    }

    @Override
    public void onResume() {
        int position = getArguments().containsKey("position") ? getArguments().getInt("position") : -1;
        android.util.Log.d("ThoLH", "TabFragment " + position + " onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        int position = getArguments().containsKey("position") ? getArguments().getInt("position") : -1;
        android.util.Log.d("ThoLH", "TabFragment " + position + " onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        int position = getArguments().containsKey("position") ? getArguments().getInt("position") : -1;
        android.util.Log.d("ThoLH", "TabFragment " + position + " onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        int position = getArguments().containsKey("position") ? getArguments().getInt("position") : -1;
        android.util.Log.d("ThoLH", "TabFragment " + position + " onDetach");
        super.onDetach();
    }
}
