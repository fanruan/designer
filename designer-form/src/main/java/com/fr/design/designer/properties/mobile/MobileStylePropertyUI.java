package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWScaleLayout;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.MobileWidgetStyleDefinePane;

public class MobileStylePropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public MobileStylePropertyUI(XCreator xCreator) {
        if(xCreator instanceof XWScaleLayout) {
            this.xCreator = xCreator.getEditingChildCreator();
        } else {
            this.xCreator = xCreator;
        }
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new MobileWidgetStyleDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return null;
    }
}
