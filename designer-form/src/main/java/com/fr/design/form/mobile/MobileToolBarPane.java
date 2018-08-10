package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.mobile.MobileRadioCheckPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.main.mobile.FormMobileAttr;


import javax.swing.*;
import java.awt.*;

/**
 * Created by 方磊 on 2016/11/8.
 */
public class MobileToolBarPane extends BasicBeanPane<FormMobileAttr> {
    //刷新选项面板
    private MobileRadioCheckPane refreshCheckPane;

    public MobileToolBarPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel toobarsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        UILabel uiLabel = new UILabel("html5");
        uiLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        this.refreshCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Refresh"));

        toobarsPane.add(uiLabel, BorderLayout.WEST);
        toobarsPane.add(refreshCheckPane, BorderLayout.EAST);
        borderPane.add(toobarsPane);
        this.add(borderPane);
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
        this.refreshCheckPane.populateBean(ob.isRefresh());
    }

    @Override
    public FormMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setRefresh(this.refreshCheckPane.updateBean());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_ToolBar");
    }

}
