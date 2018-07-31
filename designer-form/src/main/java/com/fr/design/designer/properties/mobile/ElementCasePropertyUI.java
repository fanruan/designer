package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XElementCase;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.ElementCaseDefinePane;


/**
 * Created by Administrator on 2016/5/16/0016.
 */
public class ElementCasePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public ElementCasePropertyUI(XElementCase xElementCase) {
        this.xCreator = xElementCase;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new ElementCaseDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Mobile-Attr");
    }
}
