package com.fr.design.widget.ui.designer.custom;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.Inter;

import javax.swing.*;

/**
 * Created by ibm on 2017/8/1.
 */
public class WidgetValueString implements WidgetValuePane{
    private UITextField uiTextField;

    public WidgetValueString(){
        uiTextField = new UITextField();
    }

    public JComponent createWidgetValuePane(){
        return uiTextField;
    }

    public String markTitle(){
        return Inter.getLocText("FR-Designer_Widget_String");
    }

    public void update(){

    }

    public void populate(){

    }
}