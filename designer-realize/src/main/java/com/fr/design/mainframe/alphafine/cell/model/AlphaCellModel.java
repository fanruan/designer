package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;


/**
 * Created by XiaXiang on 2017/3/23.
 */
public abstract class AlphaCellModel implements Comparable {
    private String name;
    private String content;
    private String description;
    private CellType type;
    private int searchCount;

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

    public boolean hasAction() {
        return true;
    }

    public boolean isNeedToSendToServer() {
        return true;
    }

    /**
     * model转json
     *
     * @return
     * @throws JSONException
     */
    abstract public JSONObject modelToJson() throws JSONException;

    /**
     * 获取需要保存到云中心的信息
     *
     * @return
     */
    abstract public String getStoreInformation();

    /**
     * 双击时所需执行的操作
     */
    abstract public void doAction();

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public void addSearchCount() {
        searchCount++;
    }

    @Override
    public int compareTo(Object o) {
        AlphaCellModel cellModel = (AlphaCellModel) o;
        int difference = cellModel.getSearchCount() - this.getSearchCount();
        if (difference != 0) {
            return difference;
        }
        return this.getName().compareTo(cellModel.getName());
    }

    /**
     * 恢复正常状态
     */
    public void resetState() {

    }
}
