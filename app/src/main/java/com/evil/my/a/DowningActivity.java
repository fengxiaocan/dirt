package com.evil.my.a;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evil.app.AppServiceUtils;
import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.inface.DownConstants;
import com.fxc.intface.IHandler;
import com.fxc.log.LogUtils;
import com.fxc.util.HandlerUtils;
import com.fxc.util.StringUtils;
import com.fxc.util.TimeUtils;

import static com.evil.my.inface.DownConstants.DOWN_PROGRESS_CODE;
import static com.evil.my.inface.DownConstants.DOWN_REFRESH_CODE;

public class DowningActivity extends BaseActivity {
    private TextView mTvName;
    private TextView mTvUrl;
    private TextView mTvPath;
    private TextView mTvMd5;
    private ProgressBar mProgressBar;
    private TextView mTvTotal;
    private TextView mTvFe;
    private TextView mTvTime;
    private TextView mTvStatu;
    private int mKey;

    private IHandler mIHandler = new IHandler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case DOWN_REFRESH_CODE:
                    DowningInfo info = AppServiceUtils.getService().getTaskArray().get(mKey);
                    initData(info);
                    break;
                case DOWN_PROGRESS_CODE:
                    DowningInfo info1 = AppServiceUtils.getService().getTaskArray().get(mKey);
                    update(info1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downing);
        initView();
        Bundle bundle = getBundle();
        if (bundle == null) {
            finish();
        } else {
            mKey = bundle.getInt("key", 0);
            DowningInfo info = AppServiceUtils.getService().getTaskArray().get(mKey);
            if (info != null) {
                initData(info);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        HandlerUtils.registerHandler(mIHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HandlerUtils.unregisterHandler(mIHandler);
    }

    private void initData(DowningInfo info) {
        if (info == null) {
            toast("下载已完成!");
            finish();
            return;
        }
        mTvName.setText(info.getName());
        mTvPath.setText(info.getSavePath());
        mTvMd5.setText(info.getMd5());
        mTvUrl.setText(info.getUrl());
        mTvTime.setText(TimeUtils.formatTime(info.getCreateTime(), TimeUtils.DATE_TYPE20));
        mTvTotal.setText(StringUtils.formatFileSize(info.getTotalSize()));
        update(info);
    }

    private void update(DowningInfo info) {
        if (info == null) {
            toast("下载已完成!");
            finish();
            return;
        }
        mTvFe.setText(StringUtils.formatFileSize(info.getDownSize()));
        switch (info.getStatu()) {
            default:
            case DownConstants.NoDown:
                mTvStatu.setText("未下载");
                mTvStatu.setTextColor(R.color.positiveText);
                break;
            case DownConstants.Downing:
                mTvStatu.setText("下载中");
                mTvStatu.setTextColor(R.color.textColorPrimary);
                break;
            case DownConstants.DownPause:
                mTvStatu.setText("暂停中");
                mTvStatu.setTextColor(R.color.colorPrimaryDark);
                break;
            case DownConstants.DownError:
                mTvStatu.setText("下载错误");
                mTvStatu.setTextColor(R.color.colorRed);
                break;
            case DownConstants.DownPending:
                mTvStatu.setText("等待下载");
                mTvStatu.setTextColor(R.color.darksalmon);
                break;
        }
        if (info.getDownSize() == 0) {
            mProgressBar.setMax(1);
            mProgressBar.setProgress(0);
        } else {
            mProgressBar.setMax(100);
            mProgressBar.setProgress((int) (info.getDownSize() * 100 / info.getTotalSize()));
        }
    }

    private void initView() {
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvUrl = (TextView) findViewById(R.id.tv_url);
        mTvPath = (TextView) findViewById(R.id.tv_path);
        mTvMd5 = (TextView) findViewById(R.id.tv_md5);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTvTotal = (TextView) findViewById(R.id.tv_total);
        mTvFe = (TextView) findViewById(R.id.tv_fe);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvStatu = (TextView) findViewById(R.id.tv_statu);
    }
}
