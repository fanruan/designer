package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.MobileBookMarkCombinePane;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/18
 */
public class MobileBookMarkCombinePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public MobileBookMarkCombinePropertyUI(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new MobileBookMarkCombinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
    }
}
