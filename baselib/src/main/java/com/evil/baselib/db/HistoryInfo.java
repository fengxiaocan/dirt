package com.evil.baselib.db;

import com.fxc.util.TimeUtils;

import org.litepal.crud.DataSupport;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.db
 *  @创建者: Noah.冯
 *  @时间: 20:25
 *  @描述： 历史记录
 */
public class HistoryInfo
        extends DataSupport
{
    private int    id;
    private String url;
    private String name;
    private String time;
    private long   historyTime;

    public HistoryInfo() {
    }

    public HistoryInfo(String url, String name) {
        this.url = url;
        this.name = name;
        this.time = TimeUtils.getNowTime(TimeUtils.DATE_TYPE1);
        this.historyTime = System.currentTimeMillis();
    }

    public HistoryInfo(int id, String url, String name, String time, long historyTime) {
        this.id = id;
        this.url = url;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
