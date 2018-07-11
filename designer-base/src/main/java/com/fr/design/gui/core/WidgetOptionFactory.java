package com.fr.design.gui.core;

import com.fr.base.BaseUtils;
import com.fr.form.ui.Widget;

import javax.swing.*;

public class WidgetOptionFactory {

    public static WidgetOption createByWidgetClass(String optionName, Class<? extends Widget> widgetClass) {
        return new CustomWidgetOption(optionName, BaseUtils.readIcon("/com/fr/design/images/data/user_widget.png"), widgetClass);
    }

    public static WidgetOption createByWidgetClass(String optionName, Icon optionIcon, Class<? extends Widget> widgetClass) {
        return new CustomWidgetOption(optionName, optionIcon, widgetClass);
    }
}