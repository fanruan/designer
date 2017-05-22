package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class PluginModel extends AlphaCellModel {
    private String pluginUrl;
    private String imageUrl;
    private String version;
    private String jartime;
    private String link;
    private String informationUrl;
    private int pluginId;
    private int price;
    private static final String PLUGIN_INFORMATION_URL = "http://shop.finereport.com/ShopServer?pg=plugin&pid=";
    private static final String REUSE_INFORMATION_URL = "http://shop.finereport.com/reuses/";

    public PluginModel(String name, String content, CellType type) {
        super(name, content, type);
    }
    public PluginModel(String name, String content, String imageUrl, String version, String jartime, CellType type, int price, int pluginId) {
        super(name, content);
        setType(type);
        this.pluginId = pluginId;
        if (getType() == CellType.PLUGIN) {
            this.pluginUrl = AlphaFineConstants.PLUGIN_URL + pluginId;
            this.informationUrl = PLUGIN_INFORMATION_URL + this.pluginId;
        } else {
            this.pluginUrl = AlphaFineConstants.REUSE_URL + pluginId;
            this.informationUrl = REUSE_INFORMATION_URL + this.pluginId;

        }
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

    @Override
    public JSONObject ModelToJson() {
        JSONObject object = JSONObject.create();
        try {
            object.put("result", getInformationUrl()).put("cellType", getType().getCellType());
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }

        return object;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginModel)) return false;

        PluginModel that = (PluginModel) o;

        return pluginUrl != null ? pluginUrl.equals(that.pluginUrl) : that.pluginUrl == null;
    }

    @Override
    public int hashCode() {
        return pluginUrl != null ? pluginUrl.hashCode() : 0;
    }



    public int getPluginId() {
        return pluginId;
    }

    public void setPluginId(int pluginId) {
        this.pluginId = pluginId;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }
}
