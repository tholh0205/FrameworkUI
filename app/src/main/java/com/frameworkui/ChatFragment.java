package com.frameworkui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by ThoLH on 10/05/2015.
 */
public class ChatFragment extends BaseFragment implements BaseFragment.ReusableFragment {

    private String[] data = new String[100];
    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < data.length; i++) {
            data[i] = "Test " + i;
        }
        android.util.Log.d("ThoLH", "ChatFragment savedInstanceState = " + savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasMenu(true);
        mFragmentView = inflater.inflate(R.layout.chat_fragment, container, false);
//        if (Build.VERSION.SDK_INT >= 23) {
//            getActivity().requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
//        }

        mDrawerLayout = (DrawerLayout) mFragmentView.findViewById(R.id.chat_drawer);
//        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, -1, BaseActivity.TRANSLATION_WITH_FADE_IN);
//                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.PROFILE, null, 0, false, false);
//            }
//        });
        ListView lv = (ListView) mFragmentView.findViewById(R.id.listView);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.PROFILE, null, 0, false, false);
            }
        });
        return mFragmentView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        android.util.Log.d("ThoLH", "requestCode " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onNewIntent() {
        super.onNewIntent();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.chat_menu, menu);
        MenuItem itemMore = menu.findItem(R.id.menu_more);
        itemMore.setIcon(getMenuMoreDrawable());
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
//        mFragmentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                getBaseActivity().presentFragment(BaseActivity.FragmentType.PROFILE, null, -1, BaseActivity.TRANSLATION_WITH_FADE_IN);
//                getActivity().getFragmentManagerLayout().showFragment(FragmentData.FragmentType.PROFILE, null, 0, false, false);
//            }
//        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean isEnableSwipeBack() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerVisible(Gravity.RIGHT)) {
            return false;
        }
        return super.isEnableSwipeBack();
    }
}
