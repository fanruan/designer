package com.fr.design.mainframe.widget.wrappers;


import com.fr.design.designer.properties.Encoder;

public class GridWidgetWrapper implements Encoder {

    @Override
    public String encode(Object v) {
        if (v != null) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Setting");
        }
        return null;
    }
}