package com.evil.web.intface;

/**
 * @项目名： WebBrowser
 * @包名： com.evil.webbrowser.intface
 * @创建者: Noah.冯
 * @时间: 19:59
 * @描述： TODO
 */
public interface SettingListener {
    /**
     * 后退
     */
    void onBack();

    /**
     * 前进
     */
    void onGoTo();

    /**
     * 新开浏览器
     */
    void onAdd();

    /**
     * 首页
     */
    void onGoHome();

    /**
     * 收藏
     */
    void onCollect();

    /**
     * 收藏夹
     */
    void onFavorite();

    /**
     * 历史
     */
    void onHistory();

    /**
     * 输入
     */
    void onInput();

    /**
     * 设置
     */
    void onSetting();

    /**
     * 刷新
     */
    void onRefresh();
}
