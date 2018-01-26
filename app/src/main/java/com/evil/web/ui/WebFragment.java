package com.evil.web.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.evil.app.R;
import com.evil.baselib.base.BaseFragment;
import com.evil.baselib.db.HistoryInfo;
import com.evil.web.adapter.MyDownloadListenter;
import com.evil.web.adapter.MyWebChromeClient;
import com.evil.web.adapter.MyWebViewClient;
import com.evil.web.intface.WebCallback;
import com.evil.web.intface.WebConstants;
import com.evil.web.intface.WebViewClientListener;
import com.fxc.base.dialog.DialogCancelListener;
import com.fxc.base.dialog.IosDialog;
import com.fxc.log.LogUtils;
import com.fxc.util.ClipboardUtils;
import com.fxc.util.SDCardUtils;
import com.fxc.util.SpUtils;
import com.fxc.util.StringUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import static com.evil.web.intface.WebConstants.BUNDLE_URL;
import static com.evil.web.intface.WebConstants.ENCODE_INFO;
import static com.evil.web.intface.WebConstants.IMAGE_LOAD_INFO;
import static com.evil.web.intface.WebConstants.IMAGE_WIDE_INFO;
import static com.evil.web.intface.WebConstants.JAVE_SCRIPT_WINDOW;
import static com.evil.web.intface.WebConstants.WEB_HOME;
import static com.evil.web.intface.WebConstants.WEB_HOME_URL;
import static com.evil.web.intface.WebConstants.WIN_ZOOM_INFO;
import static com.evil.web.intface.WebConstants.ZOOM_INFO;

/**
 * @项目名： WebBrowser
 * @包名： com.evil.webbrowser.ui
 * @创建者: Noah.冯
 * @时间: 12:25
 * @描述： TODO
 */
public class WebFragment
        extends BaseFragment
        implements WebViewClientListener, WebCallback {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private LinearLayout mLoadContent;
    private static boolean isGo = false;

    @Override
    public int onCreateView() {
        return R.layout.fragment_web;
    }

    @Override
    public void initView(View view, @Nullable Bundle savedInstanceState) {
        FileDownloader.setup(getContext());
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mLoadContent = (LinearLayout) view.findViewById(R.id.load_content);
        initData(savedInstanceState);
    }

    private void initData(@Nullable Bundle savedInstanceState) {
        String mUrl = null;
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(BUNDLE_URL);
        }
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.setDownloadListener(new MyDownloadListenter());
        mWebSettings = mWebView.getSettings();
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    IosDialog dialog = new IosDialog(getContext());
                    dialog.setTitle("提示");
                    dialog.setMessage("是否保存图片到本地");
                    dialog.setLeftButton("确认", new DialogCancelListener() {
                        @Override
                        public void onClick(DialogInterface dialog) {
                            super.onClick(dialog);
                            String url = hitTestResult.getExtra();
                            File file = new File(SDCardUtils.getFileOfSave("image"), StringUtils
                                    .getUrlFileName(url));
                            FileDownloader.getImpl().create(url).setPath(file.getAbsolutePath())
                                    .setListener(new FileDownloadSampleListener() {
                                        @Override
                                        protected void completed(BaseDownloadTask task) {
                                            toast("下载完成!");
                                        }

                                        @Override
                                        protected void error(BaseDownloadTask task, Throwable e) {
                                            toast("下载错误!");
                                        }
                                    }).start();
                        }
                    });
                    dialog.setRightButton("取消", new DialogCancelListener());
                    dialog.show();
                }
                return true;
            }
        });
        initSetting();
        loadUrl(mUrl);
    }

    /**
     * 初始化设置
     */
    private void initSetting() {
        boolean js = SpUtils.getInfo(WebConstants.JAVE_SCRIPT_INFO, true);
        setJs(js);
        boolean jsPopUpWindow = SpUtils.getInfo(JAVE_SCRIPT_WINDOW, true);
        if (jsPopUpWindow) {
            setWebChromeClient(new MyWebChromeClient());
        } else {
            setWebChromeClient(null);
        }
        setEncoding();
        //将图片调整到适合webview的大小
        setImageWide();
        setZoom();
        setWindowZoom();
        setImageLoad();
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        //支持内容重新布局
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置可以访问文件
        mWebSettings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        mWebSettings.setNeedInitialFocus(true);
        /*设置能使用快照*/
        mWebView.setDrawingCacheEnabled(true);
    }


    @Override
    public void loadUrl(String url) {
        if (!StringUtils.isEmpty(url)) {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
            //加载地址
            mWebView.loadUrl(url);
        } else {
            //加载地址
            mWebView.loadUrl(SpUtils.getInfo(WEB_HOME, WEB_HOME_URL));
        }
    }

    /**
     * 返回
     */
    @Override
    public boolean onGoBack() {
        isGo = true;
        //其中webView.canGoBack()在webView含有一个可后退的浏览记录时返回true
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
        return true;
    }

    /**
     * 前进
     */
    @Override
    public void onGoForward() {
        isGo = true;
        if (mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }

    /**
     * 首页
     */
    @Override
    public void onGoHome() {
        mWebView.loadUrl(SpUtils.getInfo(WEB_HOME, WEB_HOME_URL));
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        mWebView.reload();
    }

    /**
     * 设置js
     */
    @Override
    public void setJs(boolean isJs) {
        //支持js
        mWebSettings.setJavaScriptEnabled(isJs);
        //支持通过JS打开新窗口
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(isJs);
    }

    @Override
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        //支持js弹窗弹出！
        mWebView.setWebChromeClient(webChromeClient);
    }

    /**
     * 设置编码
     */
    @Override
    public void setEncoding() {
        String encoding = SpUtils.getInfo(ENCODE_INFO, "utf-8");
        //设置默认编码
        mWebSettings.setDefaultTextEncodingName(encoding);
    }

    /**
     * 设置缩放
     */
    @Override
    public void setZoom() {
        boolean zoom = SpUtils.getInfo(ZOOM_INFO, true);
        //支持缩放
        mWebSettings.setSupportZoom(zoom);
        //设置支持缩放
        mWebSettings.setBuiltInZoomControls(zoom);
    }

    /**
     * 设置屏幕缩放
     */
    @Override
    public void setWindowZoom() {
        boolean winzoom = SpUtils.getInfo(WIN_ZOOM_INFO, true);
        //缩放至屏幕的大小
        mWebSettings.setLoadWithOverviewMode(winzoom);
    }

    /**
     * 设置图片调整到适合webview的大小
     */
    @Override
    public void setImageWide() {
        boolean imagewide = SpUtils.getInfo(IMAGE_WIDE_INFO, false);
        //将图片调整到适合webview的大小
        mWebSettings.setUseWideViewPort(imagewide);
    }

    /**
     * 支持自动加载图片
     */
    @Override
    public void setImageLoad() {
        boolean imageload = SpUtils.getInfo(IMAGE_LOAD_INFO, true);
        //支持自动加载图片
        mWebSettings.setLoadsImagesAutomatically(imageload);
    }

    @Override
    public void saveWebArchive() {
        File file = SDCardUtils.getSaveDir("web");
        mWebView.saveWebArchive(file.getAbsolutePath());
        toast("保存完成!");
    }

    @Override
    public void copyUrl() {
        ClipboardUtils.copyText(mWebView.getUrl());
        toast("复制完成!");
    }

    @Override
    public String getTitle() {
        return mWebView.getTitle();
    }

    @Override
    public void lookContant() {
        mWebView.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    @Override
    public String getUrl() {
        return mWebView.getOriginalUrl();
    }

    @Override
    public Bitmap getIconBitmap() {
        return mWebView.getDrawingCache();
    }

    /**
     * 当接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();  // 接受信任所有网站的证书
        // handler.cancel();   // 默认操作 不处理
        // handler.handleMessage(null);  // 可做其他处理
    }

    /**
     * 在结束加载网页时会回调
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        if (!isGo) {
            HistoryInfo info = new HistoryInfo(mWebView.getOriginalUrl(), mWebView.getTitle());
            info.save();
            mLoadContent.setVisibility(View.GONE);
        }
        isGo = false;
        if (!view.getSettings()
                .getLoadsImagesAutomatically()) {
            view.getSettings()
                    .setLoadsImagesAutomatically(true);
        }
        dismiss();
    }

    /**
     * 在开始加载网页时会回调
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!isGo) {
            mLoadContent.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return false;
    }

    /**
     * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

        return null;
    }

    /**
     * http请求错误
     */
    @Override
    public void onReceivedHttpError(WebView view,
                                    WebResourceRequest request,
                                    WebResourceResponse errorResponse) {
    }

    /**
     * 请求错误
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    final class InJavaScriptLocalObj {

        @JavascriptInterface
        public void showSource(String html) {
            LogUtils.e("noah", html);
            IosDialog iosDialog = new IosDialog(getContext());
            iosDialog.setMessage(html);
            iosDialog.show();
        }
    }
}
