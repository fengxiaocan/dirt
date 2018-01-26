package com.evil.web.adapter;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.evil.web.intface.WebViewClientListener;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.adapter
 *  @创建者: Noah.冯
 *  @时间: 12:27
 *  @描述： TODO
 */
public class MyWebViewClient
        extends WebViewClient
{
    private WebViewClientListener mListener;



    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        mListener.onReceivedError(view, request, error);
    }

    /**
     * http请求错误
     */
    @Override
    public void onReceivedHttpError(WebView view,
                                    WebResourceRequest request,
                                    WebResourceResponse errorResponse)
    {
        mListener.onReceivedHttpError(view, request, errorResponse);
    }

    public MyWebViewClient(WebViewClientListener listener) {
        mListener = listener;
    }

    /**
     * 当接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        mListener.onReceivedSslError(view,handler,error);
    }

    /**
     * 在结束加载网页时会回调
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        mListener.onPageFinished(view,url);
    }


    /**
     * 在开始加载网页时会回调
     */
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mListener.onPageStarted(view,url,favicon);
    }



    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return mListener.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return mListener.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
     */
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
        mListener.onReceivedError(view, errorCode,description,failingUrl);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return mListener.shouldInterceptRequest(view, request);
    }
}
