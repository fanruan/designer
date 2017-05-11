package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;

import java.io.Serializable;

/**
 * Created by XiaXiang on 2017/3/23.
 */
public class AlphaCellModel implements Serializable{
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
}
