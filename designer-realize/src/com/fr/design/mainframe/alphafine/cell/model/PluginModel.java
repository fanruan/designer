package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class PluginModel extends AlphaCellModel {
    private String imageUrl;
    private String version;
    private String jartime;
    private String link;
    private String informationUrl;
    private String pluginId;
    private int id;
    private int price;

    public PluginModel(String name, String content, String imageUrl, String version, String jartime, String link, String pluginId, CellType type, int price, int id, int serchCount) {
        this(name, content, imageUrl, version, jartime, link, pluginId, type, price, id);
        setSearchCount(serchCount);
    }

    public PluginModel(String name, String content, String imageUrl, String version, String jartime, String link, String pluginId, CellType type, int price, int id) {
        super(name, content);
        this.link = link;
        setType(type);
        this.id = id;
        this.imageUrl = imageUrl;
        this.jartime = jartime;
        this.version = version;
        this.price = price;
        this.pluginId = pluginId;
        if (getType() == CellType.PLUGIN) {
            this.informationUrl = AlphaFineConstants.PLUGIN_URL + id;
        } else {
            this.informationUrl = AlphaFineConstants.REUSE_URL + id;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
            JSONObject modelObject = JSONObject.create();
            modelObject.put("name", getName()).put("description", getContent()).put("pic", getImageUrl()).put("version", getVersion()).put("jartime", getJartime()).put("id", getId()).put("pluginid", getPluginId()).put("type", getType().getTypeValue()).put("price", getPrice()).put("link", getLink()).put("searchCount", getSearchCount());
            object.put("result", modelObject).put("cellType", getType().getTypeValue());
        } catch (JSONException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }

        return object;
    }

    @Override
    public String getStoreInformation() {
        return getInformationUrl();
    }

    @Override
    public void doAction() {
    
        if (StringUtils.isBlank(this.pluginId) || !WorkContext.getCurrent().isLocal()) {
            return;
        }
        WebViewDlgHelper.showPluginInStore(getName(), "[" + ModelToJson().optString("result") + "]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PluginModel)) {
            return false;
        }
        PluginModel that = (PluginModel) o;

        return pluginId != null ? pluginId.equals(that.pluginId) : that.pluginId == null;
    }

    @Override
    public int hashCode() {
        return pluginId != null ? pluginId.hashCode() : 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }
}
