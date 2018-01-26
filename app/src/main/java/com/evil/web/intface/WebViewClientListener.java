package com.evil.web.intface;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.intface
 *  @创建者: Noah.冯
 *  @时间: 13:02
 *  @描述： TODO
 */
public interface WebViewClientListener {
    /**
     * 当接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);

    /**
     * 在结束加载网页时会回调
     */
    void onPageFinished(WebView view, String url);

    /**
     * 在开始加载网页时会回调
     */
    void onPageStarted(WebView view, String url, Bitmap favicon);



    boolean shouldOverrideKeyEvent(WebView view, KeyEvent event);


    boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request);

    /**
     * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
     */
    void onReceivedError(WebView view, int errorCode, String description, String failingUrl);



    WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request);

    /**
     * http请求错误
     */
    void onReceivedHttpError(WebView view,
                             WebResourceRequest request,
                             WebResourceResponse errorResponse);
    /**
     * 请求错误
     */
    void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
}
