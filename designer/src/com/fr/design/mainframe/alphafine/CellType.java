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

    public static CellType parse(int cellType) {
        CellType type;
        switch (cellType) {
            case 0:
                type = PLUGIN;
                break;
            case 1:
                type = DOCUMENT;
                break;
            case 2:
                type = FILE;
                break;
            case 3:
                type = ACTION;
                break;
            case 4:
                type = REUSE;
                break;
            default:
                type = FILE;
        }
        return type;

    }
    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }
}

