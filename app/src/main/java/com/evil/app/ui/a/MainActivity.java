package com.evil.app.ui.a;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.app.ui.f.MyFragment;
import com.evil.baselib.base.BaseActivity;
import com.evil.live.ui.f.LiveFragment;
import com.evil.web.ui.WebMainFragment;
import com.fxc.base.dialog.DialogCancelListener;
import com.fxc.base.dialog.IosDialog;

public class MainActivity extends BaseActivity {
    private String[] name = new String[]{"生活", "浏览器", "备忘", "发现", "我的"};
    private int[] icon = new int[]{R.drawable.mian_tabhost_one, R.drawable
            .mian_tabhost_two, R.drawable.mian_tabhost_three, R.drawable.mian_tabhost_four, R
            .drawable.mian_tabhost_five};


    private android.support.v4.app.FragmentTabHost mTabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        this.mTabHost = (FragmentTabHost) findViewById(R.id.tab_host);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.container);

        mTabHost.addTab(mTabHost.newTabSpec(name[0])
                .setIndicator(getTabHost(0)), LiveFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(name[1])
                .setIndicator(getTabHost(1)), WebMainFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(name[2])
                .setIndicator(getTabHost(2)), LiveFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(name[3])
                .setIndicator(getTabHost(3)), LiveFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(name[4])
                .setIndicator(getTabHost(4)), MyFragment.class, null);
        requestPermissions(0xAF1, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        });
    }

    private View getTabHost(int position) {
        View view = View.inflate(this, R.layout.view_main_host, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_name);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_icon);
        tv.setText(name[position]);
        iv.setImageResource(icon[position]);
        return view;
    }

    @Override
    public void onBackPressed() {
        IosDialog iosDialog = new IosDialog(this);
        iosDialog.setTitle("提示");
        iosDialog.setMessage("是否确认退出?");
        iosDialog.setRightButton("取消", new DialogCancelListener());
        iosDialog.setLeftButton("确定", new DialogCancelListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                super.onClick(dialog);
                MainActivity.super.onBackPressed();
            }
        });
        iosDialog.show();
    }
}
