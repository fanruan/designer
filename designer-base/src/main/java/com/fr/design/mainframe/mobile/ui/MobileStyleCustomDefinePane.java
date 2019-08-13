package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;
import com.fr.form.ui.mobile.MobileStyle;

import javax.swing.*;

public abstract class MobileStyleCustomDefinePane extends BasicBeanPane<MobileStyle> {

    protected Widget widget;

    public MobileStyleCustomDefinePane(Widget widget) {
        this.widget = widget;
        init();
    }

    protected abstract JPanel createPreviewPane();

    protected abstract void init();

}
