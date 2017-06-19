package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;


/**
 * Created by XiaXiang on 2017/3/23.
 */
public abstract class AlphaCellModel {
    private String name;
    private String content;
    private String description;
    private CellType type;

    public AlphaCellModel(String name, String content, CellType type) {
        this.name = name;
        this.content = content;
        this.type = type;

    }

    public AlphaCellModel(String name, String content) {
        this.name = name;
        this.content = content;

    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasNoResult() {
        return false;
    }
    /**
     * model转json
     *
     * @return
     * @throws JSONException
     */
    abstract public JSONObject ModelToJson() throws JSONException;

    /**
     * 获取需要保存到云中心的信息
     *
     * @return
     */
    abstract public String getStoreInformation();

}
