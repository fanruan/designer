package com.fr.design.extra;

import com.fr.design.dialog.BasicPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public abstract class PluginAbstractViewPane extends BasicPane {

    public JPanel createOperationPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        panel.setPreferredSize(new Dimension(120, 60));
        return panel;
    }

    @Override
    protected String title4PopupWindow() {
        return "View";
    }
}