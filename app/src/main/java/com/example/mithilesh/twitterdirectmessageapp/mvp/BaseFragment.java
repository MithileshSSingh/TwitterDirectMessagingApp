package com.example.mithilesh.twitterdirectmessageapp.mvp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class BaseFragment extends Fragment {

    public View mView;
    public BaseActivity mActivity;
    public Resources mResources;
    public ActionBar mActionBar;

    public ImageView ivArrowBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mActionBar = mActivity.getSupportActionBar();
        mResources = mActivity.getResources();

    }

    public void setScreenTitle(int stringId) {

        if (mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle(mResources.getString(stringId));
        } else if (mActivity.getActionBar() != null) {
            mActivity.getActionBar().setTitle(mResources.getString(stringId));
        }
    }

    public void setScreenTitle(String title) {

        if (mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().setTitle(title);
        } else if (mActivity.getActionBar() != null) {
            mActivity.getActionBar().setTitle(title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    protected abstract void init();

    protected abstract void initView();

    protected abstract void initMembers();

    protected abstract void initListeners();

    protected abstract void initData();
}
