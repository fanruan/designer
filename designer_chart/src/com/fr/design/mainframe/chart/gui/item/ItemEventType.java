package com.fr.design.mainframe.chart.gui.item;

import com.fr.general.ComparatorUtils;

/**
 * Created by hufan on 2016/10/11.
 */
public enum ItemEventType {
    REACTOR("reactor"),//重构选项
    DEFAULT("default")//默认选项操作
    ;

    //这个String会存起来的，不能随意更改。
    private String type;

    private ItemEventType(String type){
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    private static ItemEventType[] types;

    public static ItemEventType parse(String type){
        if(types == null){
            types = ItemEventType.values();
        }
        for(ItemEventType itemEventType : types){
            if(ComparatorUtils.equals(itemEventType.getType(), type)){
                return itemEventType;
            }
        }
        return DEFAULT;
    }
}