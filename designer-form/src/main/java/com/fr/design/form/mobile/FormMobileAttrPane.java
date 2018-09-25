package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.main.mobile.FormMobileAttr;


import javax.swing.*;
import java.awt.*;

/**
 * Created by fanglei on 2016/11/17.
 */
public class FormMobileAttrPane extends BasicBeanPane<FormMobileAttr>{
    // 模版设置面板
    private FormMobileTemplateSettingsPane formMobileTemplateSettingsPane;
    // 其他
    private FormMobileOthersPane formMobileOthersPane;

    private static final int PADDING = 10;

    public FormMobileAttrPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        panel.add(formMobileTemplateSettingsPane = new FormMobileTemplateSettingsPane());
        panel.add(formMobileOthersPane = new FormMobileOthersPane());

        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);
        UIScrollPane scrollPane = new UIScrollPane(panelWrapper);
        this.add(scrollPane);
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
        this.formMobileTemplateSettingsPane.populateBean(ob);
        this.formMobileOthersPane.populateBean(ob);
    }

    @Override
    public FormMobileAttr updateBean() {
        FormMobileAttr formMobileAttr = new FormMobileAttr();
        this.formMobileTemplateSettingsPane.updateBean(formMobileAttr);
        this.formMobileOthersPane.updateBean(formMobileAttr);
        return formMobileAttr;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
    }
}
