package com.fr.design.mainframe.alphafine;

/**
 * Created by XiaXiang on 2017/4/27.
 */
public enum CellType {
    RECOMMEND(0), ACTION(1), DOCUMENT(2), FILE(3), PLUGIN(4), REUSE(5);

    private int typeValue;
    private static final int RECOMMEND_VALUE = 0;
    private static final int ACTION_VALUE = 1;
    private static final int DOCUMENT_VALUE = 2;
    private static final int FILE_VALUE = 3;
    private static final int PLUGIN_VALUE = 4;
    private static final int REUSE_VALUE = 5;


    CellType(int type) {
        this.typeValue = type;
    }

    public static CellType parse(int typeValue) {
        CellType type;
        switch (typeValue) {
            case RECOMMEND_VALUE:
                type = RECOMMEND;
                break;
            case ACTION_VALUE:
                type = ACTION;
                break;
            case DOCUMENT_VALUE:
                type = DOCUMENT;
                break;
            case FILE_VALUE:
                type = FILE;
                break;
            case PLUGIN_VALUE:
                type = PLUGIN;
                break;
            case REUSE_VALUE:
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

