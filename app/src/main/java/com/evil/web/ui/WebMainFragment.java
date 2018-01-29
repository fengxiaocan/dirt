package com.evil.web.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.evil.app.R;
import com.evil.baselib.base.BaseFragment;
import com.evil.baselib.db.FavoriteInfo;
import com.evil.web.intface.SettingListener;
import com.evil.web.intface.WebAddCallback;
import com.fxc.auto.utils.AutoUtils;
import com.fxc.base.view.SuspendView;
import com.fxc.impl.OnSuspendClickListener;
import com.fxc.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
import static com.evil.web.ui.HistoryActivity.HISTORY_RESULT_CODE;
import static com.evil.web.ui.InputActivity.INPUT_URL_RESULT_CODE;

public class WebMainFragment extends BaseFragment implements SettingListener, WebAddCallback {
    public final static int HISTORY_REQUEST_CODE = 0x98;

    private WebFragment mCurrentWeb;
    private List<WebFragment> mWebData = new ArrayList<>();
    private SuspendView mSuspendView;
    private WebDialog mWebDialog;

    @Override
    public int onCreateView() {
        return R.layout.fragment_web_main;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        if (mCurrentWeb == null) {
            onAddWeb();
        }
    }

    private void initData() {
        if (mWebData.size() <= 10) {
            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            WebFragment webFragment = new WebFragment();
            if (mCurrentWeb != null) {
                transaction.hide(mCurrentWeb);
            }
            mCurrentWeb = webFragment;
            transaction.add(R.id.container, webFragment);
            transaction.commit();
            mWebData.add(webFragment);
        } else {
            show("只能打开10个窗口！");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSuspendView == null) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.icon_fenlei);
            mSuspendView = new SuspendView(getContext(), imageView);
            int screenWidth = AutoUtils.getScreenWidth();
            int width = screenWidth - AutoUtils.getPercentWidthSize(200);
            int screenHeight = AutoUtils.getScreenHeight();
            int height = screenHeight - AutoUtils.getPercentHeightSize(200);
            mSuspendView.setPosition(width, height);
            mSuspendView.setAlpha(0.5F);
            mSuspendView.setType(TYPE_APPLICATION_SUB_PANEL);
            mSuspendView.setWidth(AutoUtils.getPercentWidthSize(80));
            mSuspendView.setHeight(AutoUtils.getPercentWidthSize(80));
            mSuspendView.setOnClickListener(new OnSuspendClickListener() {
                @Override
                public void onClick(SuspendView suspendView) {
                    if (mWebDialog == null) {
                        mWebDialog = new WebDialog(getContext());
                    }
                    mWebDialog.setSettingListener(WebMainFragment.this);
                    mWebDialog.setWebFragment(mCurrentWeb);
                    mWebDialog.show();
                }
            });
        }
        mSuspendView.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSuspendView != null) {
            mSuspendView.cancel();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HISTORY_REQUEST_CODE) {
            if (resultCode == HISTORY_RESULT_CODE) {
                String url = data.getStringExtra("url");
                mCurrentWeb.loadUrl(url);
            } else if (resultCode == INPUT_URL_RESULT_CODE) {
                String url = data.getStringExtra("url");
                mCurrentWeb.loadUrl(url);
            }
        }
    }

    @Override
    public void onBack() {
        mCurrentWeb.onGoBack();
    }

    @Override
    public void onGoTo() {
        mCurrentWeb.onGoForward();
    }

    @Override
    public void onAdd() {
        AddDialog dialog = new AddDialog(getContext());
        dialog.setData(mWebData);
        dialog.setWebAddCallback(this);
        dialog.show();
    }

    @Override
    public void onGoHome() {
        mCurrentWeb.onGoHome();
    }

    @Override
    public void onCollect() {
        FavoriteInfo info = new FavoriteInfo();
        info.setUrl(mCurrentWeb.getUrl());
        info.setTitle(mCurrentWeb.getTitle());
        info.setTime(TimeUtils.getNowTime(TimeUtils.DATE_TYPE1));
        info.save();
        toast("添加书签成功!");
    }

    @Override
    public void onFavorite() {
        openActivityForResult(FavoriteActivity.class, HISTORY_REQUEST_CODE);
    }

    @Override
    public void onHistory() {
        openActivityForResult(HistoryActivity.class, HISTORY_REQUEST_CODE);
    }

    @Override
    public void onInput() {
        openActivityForResult(InputActivity.class, HISTORY_REQUEST_CODE);
    }

    @Override
    public void onSetting() {

    }

    @Override
    public void onRefresh() {
        mCurrentWeb.onRefresh();
    }

    @Override
    public void onClose() {
        mWebData.remove(mCurrentWeb);
        if (mWebData.size() <= 0) {
            initData();
        } else {
            FragmentManager manager = getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            WebFragment fragment = mWebData.get(0);
            transaction.show(fragment);
            mCurrentWeb = fragment;
            transaction.commit();
        }
    }

    @Override
    public void onAddWeb() {
        initData();
    }

    @Override
    public void onOpenWeb(int position) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(mCurrentWeb);
        WebFragment webFragment = mWebData.get(position);
        transaction.show(webFragment);
        mCurrentWeb = webFragment;
        transaction.commit();
    }

    @Override
    public void onCloseWeb(int position) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        WebFragment webFragment = mWebData.get(position);
        transaction.hide(webFragment);
        transaction.remove(webFragment);
        mWebData.remove(position);
        if (mWebData.size() <= 0) {
            initData();
        } else {
            WebFragment fragment = mWebData.get(0);
            transaction.show(fragment);
            mCurrentWeb = fragment;
            transaction.commit();
        }
    }
}
