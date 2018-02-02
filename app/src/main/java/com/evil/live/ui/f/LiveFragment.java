package com.evil.live.ui.f;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.evil.app.R;
import com.evil.baselib.base.BaseFragment;

public class LiveFragment extends BaseFragment {
    private WebView mWeb;

    @Override
    public int onCreateView() {
        return R.layout.fragment_live;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        mWeb = (WebView) view.findViewById(R.id.web);
        mWeb.loadUrl("file:///android_asset/adbuse.html");
    }
}
