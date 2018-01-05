package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.main.mobile.FormMobileAttr;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fanglei on 2016/11/17.
 */
public class FormMobileAttrPane extends BasicBeanPane<FormMobileAttr>{
    //工具栏容器
    private MobileToolBarPane mobileToolBarPane;
    // 模版设置面板
    private FormMobileTemplateSettingsPane formMobileTemplateSettingsPane;

    private static final int PADDING = 10;

    public FormMobileAttrPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel mobileToolBarPaneWrapper = new JPanel(new BorderLayout());
        mobileToolBarPaneWrapper.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, 0, PADDING));
        this.mobileToolBarPane = new MobileToolBarPane();
        mobileToolBarPaneWrapper.add(this.mobileToolBarPane, BorderLayout.NORTH);

        JPanel formMobileTemplateSettingsPaneWrapper = new JPanel(new BorderLayout());
        formMobileTemplateSettingsPaneWrapper.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        this.formMobileTemplateSettingsPane = new FormMobileTemplateSettingsPane();
        formMobileTemplateSettingsPaneWrapper.add(this.formMobileTemplateSettingsPane, BorderLayout.NORTH);

        this.add(formMobileTemplateSettingsPaneWrapper, BorderLayout.NORTH);
        this.add(mobileToolBarPaneWrapper, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
        this.mobileToolBarPane.populateBean(ob);
        this.formMobileTemplateSettingsPane.populateBean(ob);
    }

    @Override
    public FormMobileAttr updateBean() {
        FormMobileAttr formMobileAttr = new FormMobileAttr();
        this.mobileToolBarPane.updateBean(formMobileAttr);
        this.formMobileTemplateSettingsPane.updateBean(formMobileAttr);
        return formMobileAttr;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
