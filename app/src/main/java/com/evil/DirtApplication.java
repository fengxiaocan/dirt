package com.evil;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.evil.app.AppServiceUtils;
import com.evil.app.service.AppService;
import com.fxc.base.BaseApplication;

public class DirtApplication extends BaseApplication {
    private Intent mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppService.AppBinder binder = (AppService.AppBinder) service;
            AppService mAppService = binder.getAppService();
            AppServiceUtils.getInstance().bindAppService(mAppService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void initCreate() {
        super.initCreate();
        mService = new Intent(this, AppService.class);
        startService(mService);
        bindService(mService, mConnection, BIND_AUTO_CREATE);
    }
}
