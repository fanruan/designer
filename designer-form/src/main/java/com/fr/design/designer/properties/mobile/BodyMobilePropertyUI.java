package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.BodyMobileDefinePane;


/**
 * Created by Administrator on 2016/5/16/0016.
 */
public class BodyMobilePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public BodyMobilePropertyUI(XLayoutContainer xwFitLayout) {
        this.xCreator = xwFitLayout;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    public XCreator getxCreator() {
        return xCreator;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new BodyMobileDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
    }
}
