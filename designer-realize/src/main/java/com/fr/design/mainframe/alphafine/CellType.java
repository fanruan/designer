package com.fr.design.mainframe.alphafine;

/**
 * Created by XiaXiang on 2017/4/27.
 */
public enum CellType {
    RECOMMEND(0), ACTION(1), DOCUMENT(2), FILE(3), PLUGIN(4), REUSE(5), NO_RESULT(6), MORE(7), RECOMMEND_ROBOT(8), BOTTOM(9), ROBOT(10);

    private int typeValue;

    CellType(int type) {
        this.typeValue = type;
    }

    public static CellType parse(int typeValue) {
        for (CellType type : CellType.values()) {
            if (type.getTypeValue() == typeValue) {
                return type;
            }
        }
        return FILE;

    }

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }
}

