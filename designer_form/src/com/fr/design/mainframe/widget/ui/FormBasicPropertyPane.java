package com.fr.design.mainframe.widget.ui;

import com.fr.design.mainframe.widget.BasicPropertyPane;
import com.fr.form.ui.Widget;

/**
 * Created by ibm on 2017/8/4.
 */
public class FormBasicPropertyPane extends BasicPropertyPane {

    public void populate(Widget widget) {
        widgetName.setText(widget.getWidgetName());
    }

    public void update(Widget widget) {
        widget.setWidgetName(widgetName.getText());
    }

}
