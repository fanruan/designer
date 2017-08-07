package com.fr.design.widget.ui.designer.custom;

import javax.swing.*;

/**
 * Created by ibm on 2017/8/1.
 */
public interface WidgetValuePane{

    JComponent createWidgetValuePane();

    String markTitle();

    void update();

    void populate();
}