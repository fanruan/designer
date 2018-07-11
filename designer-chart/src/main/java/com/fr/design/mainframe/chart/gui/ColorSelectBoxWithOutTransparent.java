package com.fr.design.mainframe.chart.gui;

import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.style.color.ColorSelectPane;

/**
 * Created by Fangjie on 2016/4/8.
 * 没有透明度的颜色选择器
 */
public class ColorSelectBoxWithOutTransparent extends ColorSelectBox {
    public ColorSelectBoxWithOutTransparent(int preferredWidth){
        super(preferredWidth);
    }


    @Override
    protected ColorSelectPane getColorSelectPane(){
        return new ColorSelectPaneWithOutTransparent();
    }
}