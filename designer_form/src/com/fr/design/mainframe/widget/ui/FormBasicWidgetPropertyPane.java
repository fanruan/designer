package com.fr.design.mainframe.widget.ui;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by ibm on 2017/7/26.
 */
public class FormBasicWidgetPropertyPane extends FormBasicPropertyPane {
    private UICheckBox enableCheckBox;
    private UICheckBox visibleCheckBox;
    private XCreator xCreator;

    public FormBasicWidgetPropertyPane (XCreator xCreator){
        this.xCreator = xCreator;
        initOtherPane();
    }

    protected void initOtherPane() {
        JPanel pane2 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        pane2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        if(xCreator.supportSetEnable()){
            enableCheckBox = new UICheckBox(Inter.getLocText("Enabled"), true);
            enableCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            pane2.add(enableCheckBox);
        }
        if(xCreator.supportSetVisible()){
            visibleCheckBox = new UICheckBox(Inter.getLocText("Widget-Visible"), true);
            visibleCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            pane2.add(visibleCheckBox);
        }
        this.add(pane2, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "basicProperty";
    }

    public void populate(Widget widget) {
        super.populate(widget);
        if(xCreator.supportSetEnable()){
            enableCheckBox.setSelected(widget.isEnabled());
        }
        if(xCreator.supportSetVisible()){
            visibleCheckBox.setSelected(widget.isVisible());
        }
    }

    public void update(Widget widget) {
        super.update(widget);
        if(xCreator.supportSetEnable()){
            widget.setEnabled(enableCheckBox.isSelected());
        }
        if(xCreator.supportSetVisible()){
            widget.setVisible(visibleCheckBox.isSelected());
        }
    }

}
