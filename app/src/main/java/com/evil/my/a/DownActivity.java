package com.evil.my.a;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evil.app.AppServiceUtils;
import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.adapter.DownAdapter;
import com.fxc.intface.IHandler;
import com.fxc.log.LogUtils;
import com.fxc.util.ClipboardUtils;
import com.fxc.util.HandlerUtils;
import com.liulishuo.filedownloader.FileDownloader;

import static com.evil.app.service.AppService.DOWN_INFO_CODE;
import static com.evil.app.service.AppService.REDOWN_INFO_CODE;
import static com.evil.my.inface.DownConstants.DOWN_REFRESH_CODE;

public class DownActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mLv;
    private DownAdapter mAdapter;
    private IHandler mIHandler = new IHandler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == DOWN_REFRESH_CODE) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        initView();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv);
        mAdapter = new DownAdapter();
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HandlerUtils.registerHandler(mIHandler);
        SparseArray<DowningInfo> taskArray = AppServiceUtils.getService().getTaskArray();
        mAdapter.setTaskArray(taskArray);
    }

    @Override
    protected void onPause() {
        HandlerUtils.unregisterHandler(mIHandler);
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (mAlertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String[] items = new String[]{"开始下载", "重新下载", "暂停下载", "取消下载", "复制下载地址", "打开详情"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    DowningInfo info = mAdapter.getItem(position);
                    switch (which) {
                        case 0:
                            Message obtain = Message.obtain();
                            obtain.what = DOWN_INFO_CODE;
                            obtain.obj = info;
                            HandlerUtils.sendMessage(obtain);
                            break;
                        case 1:
                            Message message = Message.obtain();
                            message.what = REDOWN_INFO_CODE;
                            message.obj = info;
                            HandlerUtils.sendMessage(message);
                            break;
                        case 2:
                            FileDownloader.getImpl().pause(info.getId());
                            break;
                        case 3:
                            FileDownloader.getImpl().pause(info.getId());
                            FileDownloader.getImpl().clear(info.getId(), info.getSavePath());
                            AppServiceUtils.getService().getTaskArray().remove(info.getId());
                            info.delete();
                            mAdapter.setTaskArray(AppServiceUtils.getService().getTaskArray());
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 4:
                            ClipboardUtils.copyText(info.getUrl());
                            toast("复制成功!");
                            break;
                        case 5:
                            LogUtils.e("noah", "key=" + info.getId());
                            Bundle bundle = newBundle();
                            bundle.putInt("key", info.getId());
                            openActivity(DowningActivity.class, bundle);
                            break;
                    }
                }
            });
            mAlertDialog = builder.create();
        }
        mAlertDialog.show();
    }
}
