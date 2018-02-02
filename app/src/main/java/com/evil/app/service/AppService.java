package com.evil.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.evil.baselib.db.DownedInfo;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.inface.DownConstants;
import com.evil.setting.Setting;
import com.fxc.intface.IHandler;
import com.fxc.util.HandlerUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.List;

import static com.evil.my.inface.DownConstants.DOWN_PROGRESS_CODE;
import static com.evil.my.inface.DownConstants.DOWN_REFRESH_CODE;

public class AppService extends Service {
    //下载
    public static final int DOWN_INFO_CODE = 0x133;
    //重新下载
    public static final int REDOWN_INFO_CODE = 0x134;

    private SparseArray<DowningInfo> mTaskArray = new SparseArray<>();

    private IHandler mIHandler = new IHandler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case DOWN_INFO_CODE:
                    startDown((DowningInfo) message.obj);
                    break;
                case REDOWN_INFO_CODE:
                    restartDown((DowningInfo) message.obj);
                    break;
            }
        }
    };

    public class AppBinder extends Binder {

        public AppService getAppService() {
            return AppService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AppBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Setting.initSetting();
        HandlerUtils.registerHandler(mIHandler);
        FileDownloader.setup(this);
        initDownTask();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        HandlerUtils.unregisterHandler(mIHandler);
        super.onDestroy();
    }

    public SparseArray<DowningInfo> getTaskArray() {
        return mTaskArray;
    }

    private BaseDownloadTask getTask(String url, String savePath) {
        BaseDownloadTask task = FileDownloader.getImpl().create(url);
        task.setMinIntervalUpdateSpeed(1000);
        //设置任务是否只允许在Wifi网络环境下进行下载。 默认值 true
        task.setWifiRequired(Setting.sOnlyWifiDown);
        //异步回调
        task.setSyncCallback(true);
        task.setCallbackProgressMinInterval(1000);
        task.setAutoRetryTimes(Setting.sDownRetryTimes);
        task.setPath(savePath).setListener(mDownloadSampleListener);
        return task;
    }

    /**
     * 开始下载
     *
     * @param info
     */
    public void startDown(DowningInfo info) {
        DowningInfo downingInfo = mTaskArray.get(info.getId());
        BaseDownloadTask task = getTask(info.getUrl(), info.getSavePath());
        if (downingInfo !=null){
            task.start();
        }else{
            info.save();
            mTaskArray.put(task.getId(), info);
            task.start();
        }
    }

    /**
     * 重新下载
     *
     * @param info
     */
    public void restartDown(DowningInfo info) {
        BaseDownloadTask task = getTask(info.getUrl(), info.getSavePath());
        //强制重新下载
        task.setForceReDownload(true);
        info.save();
        mTaskArray.put(task.getId(), info);
        task.start();
    }

    public void initDownTask(){
        List<DowningInfo> infos = DowningInfo.findAll(DowningInfo.class);
        if (infos !=null) {
            for (DowningInfo info : infos) {
                mTaskArray.put(info.getId(),info);
            }
        }
    }

    FileDownloadSampleListener mDownloadSampleListener = new FileDownloadSampleListener() {
        @Override
        protected void completed(BaseDownloadTask task) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            DownedInfo downedInfo = DownedInfo.createDownedInfo(downInfo);
            downedInfo.save();
            downInfo.delete();
            mTaskArray.remove(task.getId());
            HandlerUtils.sendEmptyMessage(DOWN_REFRESH_CODE);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            downInfo.setStatu(DownConstants.DownError);
            HandlerUtils.sendEmptyMessage(DOWN_REFRESH_CODE);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            downInfo.setStatu(DownConstants.Downing);
            HandlerUtils.sendEmptyMessage(DOWN_REFRESH_CODE);
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            if (downInfo != null) {
                downInfo.setStatu(DownConstants.DownPending);
                HandlerUtils.sendEmptyMessage(DOWN_REFRESH_CODE);
            }
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            downInfo.setStatu(DownConstants.Downing);
            downInfo.setDownSize(soFarBytes);
            long totalSize = downInfo.getTotalSize();
            if (totalSize <= 0) {
                downInfo.setTotalSize(totalBytes);
            }
            downInfo.save();
            HandlerUtils.sendEmptyMessage(DOWN_PROGRESS_CODE);
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            DowningInfo downInfo = mTaskArray.get(task.getId());
            downInfo.setStatu(DownConstants.DownPause);
            downInfo.setDownSize(soFarBytes);
            long totalSize = downInfo.getTotalSize();
            if (totalSize <= 0) {
                downInfo.setTotalSize(totalBytes);
            }
            downInfo.save();
            HandlerUtils.sendEmptyMessage(DOWN_REFRESH_CODE);
        }

        @Override
        protected void warn(BaseDownloadTask task) {
        }
    };
}
