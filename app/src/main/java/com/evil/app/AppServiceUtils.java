package com.evil.app;

import com.evil.app.service.AppService;

public class AppServiceUtils  {
    private static final AppServiceUtils ourInstance = new AppServiceUtils();

    public static AppServiceUtils getInstance() {
        return ourInstance;
    }
    public static AppService getService() {
        return ourInstance.mAppService;
    }

    private AppServiceUtils() {
    }

    private AppService mAppService;

    public AppService getAppService() {
        return mAppService;
    }

    public void bindAppService(AppService appService) {
        mAppService = appService;
    }

    public void unbindAppService() {
        mAppService = null;
    }
}
