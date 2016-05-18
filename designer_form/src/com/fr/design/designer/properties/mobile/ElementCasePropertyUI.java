package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XElementCase;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.general.Inter;

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
        return new ElementCasePropertyTable(xCreator);
    }

    @Override
    public String tableTitle() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
