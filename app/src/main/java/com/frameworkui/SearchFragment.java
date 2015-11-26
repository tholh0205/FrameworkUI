package com.frameworkui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.frameworkui.uicore.actionbar.ActionBar;
import com.frameworkui.uicore.actionbar.ActionBarMenu;
import com.frameworkui.uicore.actionbar.ActionBarMenuItem;
import com.frameworkui.uicore.BaseFragment;
import com.frameworkui.uicore.FragmentData;
import com.frameworkui.uicore.Utils;

/**
 * Created by ThoLH on 10/26/15.
 */
public class SearchFragment extends BaseFragment {

    @Override
    public boolean isEnableSwipeBack() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hasMenu = true;
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("SearchFragment", "SearchFragment");
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.CHAT, bundle, 0, false, false);
            }
        });

        view.findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Native Dialog");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        view.findViewById(R.id.btn_frg_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogFragment dialogFragment = new AlertDialogFragment();
//                dialogFragment.show(getActivity().getSupportFragmentManager(), "1234");
            }
        });

        view.findViewById(R.id.btn_use_passcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.USE_PASSCODE = !Utils.USE_PASSCODE;
            }
        });
    }

    @Override
    public void onSetupActionBar() {
        super.onSetupActionBar();
//        setTitle("SearchFragment");
        ActionBar actionBar = (ActionBar) getView().findViewById(R.id.custom_action_bar);
        if (actionBar != null) {
            actionBar.setTitle("Search Fragment");
            actionBar.setBackButtonDrawable(getUpIndicator());
        }
    }

    @Override
    public void onCreateOptionsMenu(ActionBarMenu menu) {
        super.onCreateOptionsMenu(menu);
        menu.addItem(1, getMenuMoreDrawable()).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onTextChanged(EditText editText) {
                super.onTextChanged(editText);
                android.util.Log.d("ThoLH", "setIsSearchField " + editText.getText());
            }
        });
    }
}
