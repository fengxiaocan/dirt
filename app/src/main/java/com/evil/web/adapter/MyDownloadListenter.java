package com.evil.web.adapter;

import android.webkit.DownloadListener;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.adapter
 *  @创建者: Noah.冯
 *  @时间: 12:28
 *  @描述： 通常webview渲染的界面中含有可以下载文件的链接，点击该链接后，应该开始执行下载的操作并保存文件到本地中。
 */
public class MyDownloadListenter implements DownloadListener {
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        //下载任务...，主要有两种方式
        //1.自定义下载任务
        //2.调用系统的download的模块
//        Uri    uri    = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
    }
}
