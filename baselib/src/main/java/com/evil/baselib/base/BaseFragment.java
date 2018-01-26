package com.evil.baselib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxc.base.ui.AppFragment;

public abstract class BaseFragment extends AppFragment {
    public View rootView;// 缓存Fragment view

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(onCreateView(), null);
            initView(rootView,savedInstanceState);
        }
        return rootView;
    }

    public @LayoutRes abstract int onCreateView();

    public abstract void initView(View view,Bundle savedInstanceState);
}
