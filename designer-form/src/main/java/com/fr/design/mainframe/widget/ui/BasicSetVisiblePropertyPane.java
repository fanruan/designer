package com.fr.design.mainframe.widget.ui;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by kerry on 2017/9/30.
 */
public class BasicSetVisiblePropertyPane extends FormBasicPropertyPane {
    private UICheckBox visibleCheckBox;

    public BasicSetVisiblePropertyPane() {
        initComponent();
    }

    protected void initComponent() {
        JPanel pane2 = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
        pane2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        UICheckBox otherOtherConfig = createOtherConfig();
        if(otherOtherConfig != null){
            pane2.add(otherOtherConfig);
        }
        visibleCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Widget_Visible"), true);
        visibleCheckBox.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Basic"));

        visibleCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pane2.add(visibleCheckBox);
        this.add(pane2, BorderLayout.CENTER);
    }

    public UICheckBox createOtherConfig(){
        return null;
    }

    public void populate(Widget widget) {
        super.populate(widget);
        visibleCheckBox.setSelected(widget.isVisible());
    }

    public void update(Widget widget) {
        super.update(widget);
        widget.setVisible(visibleCheckBox.isSelected());
    }


}
