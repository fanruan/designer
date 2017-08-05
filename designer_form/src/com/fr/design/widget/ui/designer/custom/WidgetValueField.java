package com.fr.design.widget.ui.designer.custom;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/1.
 */
public class WidgetValueField implements WidgetValuePane{
    private UITextField dataSource;
    private UITextField field;

    public WidgetValueField(){
        dataSource = new UITextField();
        field = new UITextField();
    }

    public JComponent createWidgetValuePane(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(1,7));
        jPanel.add(dataSource, BorderLayout.NORTH);
        jPanel.add(field, BorderLayout.CENTER);
        return jPanel;
    }

    public String markTitle(){
        return Inter.getLocText("FR-Designer_Widget_Field");
    }

    public void update(){
        //todo
    }

    public void populate(){
        //todo
    }
}
