package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.designer.properties.Encoder;
import com.fr.design.designer.properties.NameWithListeners;


public class EventHandlerWrapper implements Encoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        } else {
            NameWithListeners handler = (NameWithListeners) v;
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Page_Total") + handler.getCountOfListeners4ThisName() + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Ge") + handler.getName() + com.fr.design.i18n.Toolkit.i18nText("Event");
        }
    }
}
