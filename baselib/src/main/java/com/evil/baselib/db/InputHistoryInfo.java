package com.evil.baselib.db;

import com.fxc.util.TimeUtils;

import org.litepal.crud.DataSupport;

/**
 * @项目名： WebBrowser
 * @包名： com.evil.webbrowser.db
 * @创建者: Noah.冯
 * @时间: 20:25
 * @描述： 输入历史记录
 */
public class InputHistoryInfo
        extends DataSupport {
    private int id;
    private String url;
    private String time;
    private long historyTime;

    public InputHistoryInfo() {
    }

    public InputHistoryInfo(String url) {
        this.url = url;
        this.time = TimeUtils.getNowTime(TimeUtils.DATE_TYPE1);
        this.historyTime = System.currentTimeMillis();
    }

    public InputHistoryInfo(int id, String url, String time, long historyTime) {
        this.id = id;
        this.url = url;
        this.time = time;
        this.historyTime = historyTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(long historyTime) {
        this.historyTime = historyTime;
    }
}
