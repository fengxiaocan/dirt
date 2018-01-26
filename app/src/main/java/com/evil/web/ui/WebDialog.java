package com.evil.web.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.web.intface.SettingListener;
import com.fxc.util.SpUtils;

import static com.evil.web.intface.WebConstants.JAVE_SCRIPT_INFO;

public class WebDialog extends Dialog implements View.OnClickListener {
    private ImageView mIvBack;
    private ImageView mIvArrow;
    private ImageView mIvRefresh;
    private ImageView mIvInfo;
    private ImageView mIvClose;
    private TextView mTvInput;
    private TextView mTvNew;
    private TextView mTvHome;
    private TextView mTvBookmark;
    private TextView mTvOthreBookmark;
    private TextView mTvLook;
    private TextView mTvSet;
    private TextView mTvCopy;
    private TextView mTvSave;
    private CheckBox mTvJs;
    private WebFragment mCurrentWeb;
    private SettingListener mSettingListener;

    public WebDialog(@NonNull Context context) {
        super(context);

        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_setting);
        this.mIvBack = (ImageView) findViewById(R.id.iv_back);
        this.mIvArrow = (ImageView) findViewById(R.id.iv_arrow);
        this.mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        this.mIvInfo = (ImageView) findViewById(R.id.iv_info);
        this.mIvClose = (ImageView) findViewById(R.id.iv_close);
        this.mTvInput = (TextView) findViewById(R.id.tv_input);
        this.mTvNew = (TextView) findViewById(R.id.tv_new);
        this.mTvHome = (TextView) findViewById(R.id.tv_home);
        this.mTvBookmark = (TextView) findViewById(R.id.tv_bookmark);
        this.mTvOthreBookmark = (TextView) findViewById(R.id.tv_other_bookmark);
        this.mTvLook = (TextView) findViewById(R.id.tv_look);
        this.mTvSet = (TextView) findViewById(R.id.tv_set);
        this.mTvCopy = (TextView) findViewById(R.id.tv_copy);
        this.mTvSave = (TextView) findViewById(R.id.tv_save);
        this.mTvJs = (CheckBox) findViewById(R.id.tv_js);
        initEvent();
    }

    private void initEvent() {
        setOnClick(mIvBack, mIvArrow, mIvRefresh, mIvInfo, mIvClose, mTvInput, mTvNew, mTvHome,
                mTvBookmark, mTvOthreBookmark, mTvLook, mTvSet, mTvCopy, mTvSave,mTvJs);
    }

    private void setOnClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void show() {
        boolean info = SpUtils.getInfo(JAVE_SCRIPT_INFO, true);
        this.mTvJs.setChecked(info);
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams l = window.getAttributes();
        l.gravity = Gravity.RIGHT | Gravity.CENTER;
        window.setAttributes(l);
    }

    public void setWebFragment(WebFragment fragment) {
        mCurrentWeb = fragment;
    }

    public void setSettingListener(SettingListener settingListener) {
        mSettingListener = settingListener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.iv_back:
                //其中webView.canGoBack()在webView含有一个可后退的浏览记录时返回true
                mCurrentWeb.onGoBack();
                break;
            case R.id.iv_arrow:
                mCurrentWeb.onGoForward();
                break;
            case R.id.tv_new:
                mSettingListener.onAdd();
                break;
            case R.id.tv_home:
                mCurrentWeb.onGoHome();
                break;
            case R.id.iv_refresh:
                mCurrentWeb.onRefresh();
                break;
            case R.id.iv_info:
                break;
            case R.id.iv_close:
                break;
            case R.id.tv_input:
                break;
            case R.id.tv_bookmark:
                break;
            case R.id.tv_other_bookmark:
                break;
            case R.id.tv_look:
                mCurrentWeb.lookContant();
                break;
            case R.id.tv_set:
                break;
            case R.id.tv_copy:
                mCurrentWeb.copyUrl();
                break;
            case R.id.tv_save:
                mCurrentWeb.saveWebArchive();
                break;
            case R.id.tv_js:
                boolean info = SpUtils.getInfo(JAVE_SCRIPT_INFO, true);
                SpUtils.save(JAVE_SCRIPT_INFO,!info);
                mCurrentWeb.setJs(!info);
                break;
        }
    }
}
