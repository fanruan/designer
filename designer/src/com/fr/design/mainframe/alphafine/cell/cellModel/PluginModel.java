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

    @Override
    public JSONObject ModelToJson() {
        JSONObject object = JSONObject.create();
        try {
            object.put("name", getName()).put("content", getContent()).put("pluginUrl", getPluginUrl()).put("imageUrl", getImageUrl()).put("cellType", getType().getCellType()).put("jartime", getJartime()).put("version", getVersion()).put("price", getPrice());
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

    public static PluginModel jsonToModel(JSONObject object) {
        String name = object.optString("name");
        String content = object.optString("description");
        String pluginUrl = AlphaFineConstants.REUSE_URL + object.optString("id");
        String imageUrl = null;
        try {
            imageUrl = AlphaFineConstants.PLUGIN_IMAGE_URL + URLEncoder.encode(object.optString("pic").toString().substring(AlphaFineConstants.PLUGIN_IMAGE_URL.length()), "utf8");
        } catch (UnsupportedEncodingException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        String version = null;
        String jartime = null;
        CellType type = CellType.REUSE;
        String link = object.optString("link");
        if (ComparatorUtils.equals(link, "plugin")) {
            version = object.optString("version");
            jartime = object.optString("jartime");
            type = CellType.PLUGIN;
            pluginUrl = AlphaFineConstants.PLUGIN_URL + object.optString("id");
        }
        int price = object.optInt("price");
        return new PluginModel(name, content, pluginUrl, imageUrl, version, jartime, type, price);
    }
}
