package com.fr.design.mainframe.alphafine;

/**
 * Created by XiaXiang on 2017/4/27.
 */
public enum CellType {
    RECOMMEND(0), ACTION(1), DOCUMENT(2), FILE(3), PLUGIN(4), REUSE(5);

    private int typeValue;

    CellType(int type) {
        this.typeValue = type;
    }

    public static CellType parse(int cellType) {
        CellType type;
        switch (cellType) {
            case 0:
                type = RECOMMEND;
                break;
            case 1:
                type = ACTION;
                break;
            case 2:
                type = DOCUMENT;
                break;
            case 3:
                type = FILE;
                break;
            case 4:
                type = PLUGIN;
                break;
            case 5:
                type = REUSE;
                break;
            default:
                type = FILE;
        }
        return type;

    }
    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }
}

