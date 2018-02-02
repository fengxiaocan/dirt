package com.evil.baselib.db;

import com.fxc.key.Md5Utils;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * @name： LiveExplorer
 * @package： com.evil.live.db
 * @author: Noah.冯 QQ:1066537317
 * @time: 14:53
 * @version: 1.1
 * @desc： TODO
 */

public class DownedInfo extends DataSupport {
    private int id;
    private String name;
    private String md5;
    private String url;
    private String savePath;
    private long downTime;
    private long totalSize;

    public static DownedInfo createDownedInfo(DowningInfo infos) {
        DownedInfo info = new DownedInfo();
        info.setId(infos.getId());
        info.setDownTime(System.currentTimeMillis());
        info.setUrl(infos.getUrl());
        info.setName(infos.getName());
        info.setTotalSize(infos.getTotalSize());
        info.setSavePath(infos.getSavePath());
        return info;
    }


    public void createId() {
        id = FileDownloadUtils.generateId(url, savePath);
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.md5 = Md5Utils.codeMd5(url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownedInfo downInfo = (DownedInfo) o;

        if (md5 != null ? !md5.equals(downInfo.md5) : downInfo.md5 != null) return false;
        return url != null ? url.equals(downInfo.url) : downInfo.url == null;
    }

    @Override
    public int hashCode() {
        int result = md5.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void delect() {
        new File(savePath).delete();
    }
}
