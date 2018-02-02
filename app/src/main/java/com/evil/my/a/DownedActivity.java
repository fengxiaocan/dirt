package com.evil.my.a;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.baselib.db.DownedInfo;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.adapter.DownedAdapter;
import com.fxc.base.dialog.DialogCancelListener;
import com.fxc.base.dialog.IosDialog;
import com.fxc.intface.IHandler;
import com.fxc.util.ClipboardUtils;
import com.fxc.util.HandlerUtils;

import java.io.File;
import java.util.List;

import static com.evil.app.service.AppService.REDOWN_INFO_CODE;
import static com.evil.my.inface.DownConstants.DOWN_REFRESH_CODE;

public class DownedActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mLv;
    private DownedAdapter mAdapter;
    private IHandler mIHandler = new IHandler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == DOWN_REFRESH_CODE) {
                List<DownedInfo> list = DownedInfo.findAll(DownedInfo.class);
                mAdapter.setData(list);
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
        mAdapter = new DownedAdapter();
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HandlerUtils.registerHandler(mIHandler);
        List<DownedInfo> list = DownedInfo.findAll(DownedInfo.class);
        mAdapter.setData(list);
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
            String[] items = new String[]{"重新下载", "删除下载", "复制下载地址"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    final DownedInfo info = mAdapter.getItem(position);
                    switch (which) {
                        case 0:
                            DowningInfo info1 = DowningInfo.createDownInfo(info);
                            Message message = Message.obtain();
                            message.what = REDOWN_INFO_CODE;
                            message.obj = info1;
                            HandlerUtils.sendMessage(message);
                            break;
                        case 1:
                            IosDialog iosDialog = new IosDialog(getContext());
                            iosDialog.setMessage("是否删除源文件?");
                            iosDialog.setLeftButton("删除", new DialogCancelListener() {
                                @Override
                                public void onClick(DialogInterface dialog) {
                                    super.onClick(dialog);
                                    File file = new File(info.getSavePath());
                                    file.delete();
                                    info.delete();
                                    mAdapter.removeT(position);
                                }
                            });
                            iosDialog.setRightButton("不删除", new DialogCancelListener() {
                                @Override
                                public void onClick(DialogInterface dialog) {
                                    super.onClick(dialog);
                                    info.delete();
                                    mAdapter.removeT(position);
                                }
                            });
                            iosDialog.show();
                            break;
                        case 2:
                            ClipboardUtils.copyText(info.getUrl());
                            toast("复制成功!");
                            break;
                    }
                }
            });
            mAlertDialog = builder.create();
        }
        mAlertDialog.show();
    }
}
