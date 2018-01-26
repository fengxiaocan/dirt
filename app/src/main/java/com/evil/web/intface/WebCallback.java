package com.evil.web.intface;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.ui
 *  @创建者: Noah.冯
 *  @时间: 13:14
 *  @描述： 浏览器回调
 */
public interface WebCallback {

    void loadUrl(String url);

    /**
     * 返回
     */
    boolean onGoBack();

    /**
     * 前进
     */
    void onGoForward();

    /**
     * 首页
     */
    void onGoHome();

    /**
     * 刷新
     */
    void onRefresh();

    /**
     * 设置js
     */
    void setJs(boolean isJs);

    /**
     * 设置js
     */
    void setWebChromeClient(WebChromeClient webChromeClient);

    /**
     * 设置编码
     */
    void setEncoding();

    /**
     * 设置缩放
     */
    void setZoom();

    /**
     * 设置屏幕缩放
     */
    void setWindowZoom();

    /**
     * 设置图片调整到适合webview的大小
     */
    void setImageWide();

    /**
     * 支持自动加载图片
     */
    void setImageLoad();
    /**
     * 保存网页
     */
    void saveWebArchive();
    /**
     * 复制网址
     */
    void  copyUrl();

    /**
     * 获取当前标题
     */
    String getTitle();

    /**
     * 查看原网页
     */
    void lookContant();

    /**
     * 获取当前地址
     */
    String getUrl();

    /**
     * 获取当前截图
     */
    Bitmap getIconBitmap();
}
