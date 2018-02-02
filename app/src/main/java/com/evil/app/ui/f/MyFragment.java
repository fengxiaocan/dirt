package com.evil.app.ui.f;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.baselib.base.BaseFragment;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.a.DownActivity;
import com.evil.my.a.DownedActivity;
import com.fxc.base.dialog.InputDialog;
import com.fxc.base.dialog.InputResultListener;
import com.fxc.util.HandlerUtils;

import static com.evil.app.service.AppService.DOWN_INFO_CODE;

public class MyFragment extends BaseFragment {

    private ViewHolder mHolder;
    private InputDialog mInputDialog;

    @Override
    public int onCreateView() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mHolder = new ViewHolder(view);
        mHolder.mTvDown.setOnClickListener(this);
        mHolder.mTvDowned.setOnClickListener(this);
        mHolder.mTvNewDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_down:
                openActivity(DownActivity.class);
                break;
            case R.id.tv_downed:
                openActivity(DownedActivity.class);
                break;
            case R.id.tv_new_down:
                if (mInputDialog == null) {
                    mInputDialog = new InputDialog(getContext());
                    mInputDialog.setTitle("下载提示");
                    mInputDialog.setInputHint("请输入下载地址");
                    mInputDialog.setInputResultListener(new InputResultListener() {
                        @Override
                        public void onResult(String url) {
                            DowningInfo downInfo = DowningInfo.createDownInfo(url);
                            Message obtain = Message.obtain();
                            obtain.what = DOWN_INFO_CODE;
                            obtain.obj = downInfo;
                            HandlerUtils.sendMessage(obtain);
                        }
                    });
                }
                mInputDialog.show();
                break;
        }
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mTvSetPw;
        public TextView mTvSet;
        public TextView mTvPeople;
        public TextView mTvDown;
        public TextView mTvDowned;
        public TextView mTvNewDown;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvSetPw = (TextView) rootView.findViewById(R.id.tv_set_pw);
            this.mTvSet = (TextView) rootView.findViewById(R.id.tv_set);
            this.mTvPeople = (TextView) rootView.findViewById(R.id.tv_people);
            this.mTvDown = (TextView) rootView.findViewById(R.id.tv_down);
            this.mTvDowned = (TextView) rootView.findViewById(R.id.tv_downed);
            this.mTvNewDown = (TextView) rootView.findViewById(R.id.tv_new_down);
        }

    }
}
