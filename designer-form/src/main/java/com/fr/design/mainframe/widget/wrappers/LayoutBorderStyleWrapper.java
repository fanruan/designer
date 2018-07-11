/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.design.gui.xpane.LayoutBorderPane;
import com.fr.form.ui.LayoutBorderStyle;

public class LayoutBorderStyleWrapper implements Encoder, Decoder {
    public LayoutBorderStyleWrapper() {
        
    }

    /**
     * 将属性转化成字符串
     * @param v    属性对象
     * @return      字符串
     */
    public String encode(Object v) {
       if (v == null) {
           return null;
       }
       LayoutBorderStyle style = (LayoutBorderStyle)v;
       return LayoutBorderPane.BORDER_TYPE[style.getType()];
    }

    /**
     * 将字符串转化成属性
     * @param txt  字符串
     * @return  属性对象
     */
    public Object decode(String txt) {
        return null;
    }

    /**
     *  符合规则
     * @param txt   字符串
     * @throws ValidationException    抛错
     */
    public void validate(String txt) throws ValidationException {
        
    }

}