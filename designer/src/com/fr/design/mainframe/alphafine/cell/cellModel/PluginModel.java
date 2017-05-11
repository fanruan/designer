package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.ComparatorUtils;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class PluginModel extends AlphaCellModel {
    private String pluginUrl;
    private String imageUrl;
    private String version;
    private String jartime;
    private String link;
    private int price;

    public PluginModel(String name, String content, CellType type) {
        super(name, content, type);
    }
    public PluginModel(String name, String content, String pluginUrl, String imageUrl, String version, String jartime, CellType type, int price) {
        super(name, content);
        setType(type);
        this.pluginUrl = pluginUrl;
        this.imageUrl = imageUrl;
        this.jartime = jartime;
        this.version = version;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPluginUrl() {
        return pluginUrl;
    }

    public void setPluginUrl(String pluginUrl) {
        this.pluginUrl = pluginUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String verSion) {
        this.version = verSion;
    }

    public String getJartime() {
        return jartime;
    }

    public void setJarTime(String jarTime) {
        this.jartime = jarTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
