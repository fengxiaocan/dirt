package com.evil.web.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.fxc.base.dialog.DialogCancelListener;
import com.fxc.base.dialog.IosDialog;
import com.fxc.util.ClipboardUtils;
import com.fxc.util.SpUtils;
import com.fxc.util.ToastUtils;

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
    private TextView mTvSearch;
    private TextView mTvAdd;
    private TextView mTvHostory;
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
        this.mTvSearch = (TextView) findViewById(R.id.tv_search);
        this.mTvAdd = (TextView) findViewById(R.id.tv_add);
        this.mTvHostory = (TextView) findViewById(R.id.tv_history);
        this.mTvLook = (TextView) findViewById(R.id.tv_look);
        this.mTvSet = (TextView) findViewById(R.id.tv_set);
        this.mTvCopy = (TextView) findViewById(R.id.tv_copy);
        this.mTvSave = (TextView) findViewById(R.id.tv_save);
        this.mTvJs = (CheckBox) findViewById(R.id.tv_js);
        initEvent();
    }

    private void initEvent() {
        setOnClick(mIvBack, mIvArrow, mIvRefresh, mIvInfo, mIvClose, mTvInput, mTvNew, mTvHome,
                mTvBookmark, mTvHostory, mTvLook, mTvSet, mTvCopy, mTvSave,mTvJs,mTvSearch,mTvAdd);
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
                mSettingListener.onBack();
                break;
            case R.id.iv_arrow:
                mCurrentWeb.onGoForward();
                break;
            case R.id.tv_new:
                mSettingListener.onAdd();
                break;
            case R.id.tv_add:
                mSettingListener.onCollect();
                break;
            case R.id.tv_home:
                mSettingListener.onGoHome();
                break;
            case R.id.iv_refresh:
                mSettingListener.onRefresh();
                break;
            case R.id.iv_info:
                IosDialog iosDialog = new IosDialog(getContext());
                iosDialog.setLeftButton("复制",new DialogCancelListener(){
                    @Override
                    public void onClick(DialogInterface dialog) {
                        super.onClick(dialog);
                        String url = mCurrentWeb.getUrl();
                        ClipboardUtils.copyText(url);
                        ToastUtils.showShort("复制成功");
                    }
                });
                iosDialog.setRightButton("取消",new DialogCancelListener());
                iosDialog.setTitle(mCurrentWeb.getTitle());
                iosDialog.setMessage(mCurrentWeb.getUrl());
                iosDialog.show();
                break;
            case R.id.tv_search:
                mCurrentWeb.loadUrl("http://www.baidu.com");
                break;
            case R.id.iv_close:
                mSettingListener.onClose();
                break;
            case R.id.tv_input:
                mSettingListener.onInput();
                break;
            case R.id.tv_bookmark:
                mSettingListener.onFavorite();
                break;
            case R.id.tv_history:
                mSettingListener.onHistory();
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
