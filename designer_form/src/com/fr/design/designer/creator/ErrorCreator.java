package com.fr.design.designer.creator;

import com.fr.form.ui.ErrorMarker;
import com.fr.form.ui.Widget;
import com.fr.stable.StringUtils;

import java.awt.*;

/**
 * Created by richie on 2017/6/28.
 */
public class ErrorCreator  extends NullCreator {

    public ErrorCreator(Widget widget, Dimension initSize) {
        super(widget, initSize);
    }


    @Override
    protected String showText() {
        ErrorMarker marker = (ErrorMarker)toData();
        String text = marker.getWidgetValue().getDisplayValue();
        if (StringUtils.isEmpty(text)) {
            return super.showText();
        }
        return text;
    }
}
