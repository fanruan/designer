package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.CellType;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class MoreModel {
    private String name;
    private boolean needMore;
    private String content;
    private CellType type;
    private boolean isLoading;

    public MoreModel(String name, String content, boolean needMore, CellType type) {
        this.name = name;
        this.needMore = needMore;
        this.content = content;
        this.type = type;
    }

    public MoreModel(String name, CellType type) {
        this.name = name;
        this.needMore = false;
        this.type = type;
    }

    public MoreModel(String name) {
        this.name = name;
        this.isLoading = true;
    }
    public MoreModel(String name, boolean isLoading) {
        this.name = name;
        this.isLoading = isLoading;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNeedMore() {
        return needMore;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
