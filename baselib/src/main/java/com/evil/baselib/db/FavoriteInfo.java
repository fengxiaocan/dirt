package com.evil.baselib.db;

import org.litepal.crud.DataSupport;

public class FavoriteInfo extends DataSupport {
    private String title;
    private String url;
    private String time;
    private long   historyTime;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
