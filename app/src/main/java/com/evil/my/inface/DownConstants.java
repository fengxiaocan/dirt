package com.evil.my.inface;

public interface DownConstants {
    //未下载
    int NoDown = 0;
    //正在下载
    int Downing = 1;
    //下载暂停
    int DownPause = 2;
    //下载错误
    int DownError = 3;
    //等待下载
    int DownPending = 4;

    //刷新
    int DOWN_REFRESH_CODE = 0x111;

    int DOWN_PROGRESS_CODE = 0x112;
}
