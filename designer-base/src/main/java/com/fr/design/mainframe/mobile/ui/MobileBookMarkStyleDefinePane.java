package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.MobileBookMarkStyleProvider;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.mobile.MobileBookMarkStyle;
import com.fr.general.ComparatorUtils;
import com.fr.invoke.Reflect;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/23
 */
public class MobileBookMarkStyleDefinePane extends BasicBeanPane<MobileBookMarkStyle> {

    private BasicBeanPane<MobileBookMarkStyle> customDefinePane;
    private String displayName;

    MobileBookMarkStyleDefinePane(MobileBookMarkStyleProvider bookMarkStyleProvider) {
        this.customDefinePane = Reflect.on(
                bookMarkStyleProvider.classForMobileBookMarkStyleAppearance()).create().get();
        this.displayName = bookMarkStyleProvider.displayName();
        initComponent();
    }

    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel settingPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        if (!ComparatorUtils.equals(displayName, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_None_BookMark_Style"))) {
            UILabel hintLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Hint"));
            hintLabel.setForeground(Color.GRAY);
            settingPane.add(hintLabel, BorderLayout.NORTH);
        }
        settingPane.add(this.customDefinePane, BorderLayout.CENTER);
        this.add(settingPane, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(MobileBookMarkStyle ob) {
        this.customDefinePane.populateBean(ob);
    }

    @Override
    public MobileBookMarkStyle updateBean() {
        return this.customDefinePane.updateBean();
    }

    @Override
    protected String title4PopupWindow() {
        return "MobileBookMarkStyleDefinePane";
    }
}
