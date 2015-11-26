package com.frameworkui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frameworkui.uicore.BaseFragment;

/**
 * Created by ThoLH on 11/20/15.
 */
public class PasscodeFragment extends BaseFragment implements BaseFragment.SingleInstance {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.passcode_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFragment(false);
            }
        });
    }

    @Override
    public boolean isEnableSwipeBack() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
