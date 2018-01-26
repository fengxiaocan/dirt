package com.evil.baselib.base;

import com.fxc.base.ui.AppActivity;

public class BaseActivity extends AppActivity {
    @Override
    public boolean isCanSwipeBack() {
        return false;
    }
}
