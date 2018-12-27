package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XTextEditor;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.ScanCodeMobileDefinePane;

public class ScanCodeMobilePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public ScanCodeMobilePropertyUI(XTextEditor xTextEditor) {
        this.xCreator = xTextEditor;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new ScanCodeMobileDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr");
    }
}
