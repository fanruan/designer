package com.fr.design.mainframe.alphafine;

/**
 * Created by XiaXiang on 2017/4/27.
 */
public enum CellType {
    PLUGIN(0), DOCUMENT(1), FILE(2), ACTION(3), REUSE(4);


    private int cellType;
    CellType(int type) {
        this.cellType = type;
    }


    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }
}

