package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.designer.properties.Encoder;
import com.fr.design.designer.properties.NameWithListeners;
import com.fr.general.Inter;

public class EventHandlerWrapper implements Encoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        } else {
            NameWithListeners handler = (NameWithListeners) v;
            return Inter.getLocText("Page_Total") + handler.getCountOfListeners4ThisName() + Inter.getLocText("Ge") + handler.getName() + Inter.getLocText("Event");
        }
    }
}