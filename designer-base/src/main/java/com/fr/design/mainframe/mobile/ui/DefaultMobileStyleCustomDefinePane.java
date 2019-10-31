package com.fr.design.mainframe.mobile.ui;

import com.fr.form.ui.Widget;
import com.fr.form.ui.mobile.MobileStyle;

import javax.swing.*;

public class DefaultMobileStyleCustomDefinePane extends MobileStyleCustomDefinePane {


    public DefaultMobileStyleCustomDefinePane(Widget widget) {
        super(widget);
    }

    @Override
    protected JPanel createPreviewPane() {
        return null;
    }

    @Override
    public void populateBean(MobileStyle ob) {

    }

    @Override
    public MobileStyle updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    protected void init() {

    }
}
