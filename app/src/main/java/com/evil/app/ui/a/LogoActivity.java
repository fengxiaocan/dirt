package com.evil.app.ui.a;

import com.evil.app.R;

public class LogoActivity extends com.fxc.base.ui.LogoActivity {
    @Override
    public void onCreate() {
        setContentView(R.layout.activity_logo);
    }

    @Override
    public void runTask() {
        openActivity(MainActivity.class,true);
    }

    @Override
    public long taskTime() {
        return 1000;
    }
}
