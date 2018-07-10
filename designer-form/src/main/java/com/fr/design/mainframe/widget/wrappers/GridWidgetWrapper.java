package com.fr.design.mainframe.widget.wrappers;

import com.fr.general.Inter;
import com.fr.design.designer.properties.Encoder;

public class GridWidgetWrapper implements Encoder {

    @Override
    public String encode(Object v) {
        if (v != null) {
            return Inter.getLocText(new String[]{"Widget", "Set"});
        }
        return null;
    }
}