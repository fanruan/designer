package com.fr.design.mainframe.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/7/26.
 */
public class FormBasicWidgetPropertyPane extends FormBasicPropertyPane {
    private UICheckBox enableCheckBox;
    private UICheckBox visibleCheckBox;

    public FormBasicWidgetPropertyPane (){
        initOtherPane();
    }

    protected void initOtherPane() {
        JPanel pane2 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        enableCheckBox = new UICheckBox(Inter.getLocText("Enabled"), true);
        pane2.add(enableCheckBox);
        visibleCheckBox = new UICheckBox(Inter.getLocText("Widget-Visible"), true);
        pane2.add(visibleCheckBox);
        this.add(pane2, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "basicProperty";
    }

    public void populate(Widget widget) {
        enableCheckBox.setSelected(widget.isEnabled());
        visibleCheckBox.setSelected(widget.isVisible());
    }

    public void update(Widget widget) {
        widget.setEnabled(enableCheckBox.isSelected());
        widget.setEnabled(visibleCheckBox.isSelected());
    }

}
