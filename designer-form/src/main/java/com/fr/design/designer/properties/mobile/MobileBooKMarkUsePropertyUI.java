package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.MobileBookMarkDefinePane;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/12
 */
public class MobileBooKMarkUsePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public MobileBooKMarkUsePropertyUI(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new MobileBookMarkDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
    }
}
