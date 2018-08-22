package com.fr.design.onlineupdate.domain;

import com.fr.general.ComparatorUtils;
import com.fr.json.JSONObject;

import java.util.Date;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class DownloadItem {

    //显示为百分比
    private static final int PERCENTAGE_RATIO = 100;
    //显示kB
    private static final int BYTETOKB_RATIO = 1000;

    private String name;
    private String url;
    private long size;

    private int totalLength;
    private int downloadLength;

    public DownloadItem(JSONObject json) {
        this(json.optString("name"), json.optString("url"), json.optLong("size"));
    }

    public DownloadItem(String name, String url, long size) {
        this.name = name;
        this.url = url;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url + "?v=" + new Date().getTime();
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getDownloadLength() {
        return downloadLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public void setDownloadLength(int downloadLength) {
        this.downloadLength = downloadLength;
    }

    public int getProgressValue() {
        return (int) ((downloadLength / (double) totalLength) * PERCENTAGE_RATIO);
    }

    public String getProgressString() {
        return downloadLength / BYTETOKB_RATIO + "KB/" + totalLength / BYTETOKB_RATIO + "KB";
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "name:" + name + ";download:" + getProgressString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DownloadItem
                && ComparatorUtils.equals(((DownloadItem) obj).name, name)
                && ComparatorUtils.equals(((DownloadItem) obj).url, url);
    }

    /**
     * 返回一个hash码
     *
     * @return hash码
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}