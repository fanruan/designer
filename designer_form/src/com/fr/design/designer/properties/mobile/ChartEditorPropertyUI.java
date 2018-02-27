package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XChartEditor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XElementCase;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.ChartEditorDefinePane;
import com.fr.design.widget.ui.designer.mobile.ElementCaseDefinePane;
import com.fr.general.Inter;

/**
 * Created by plough on 2018/1/18.
 */
public class ChartEditorPropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public ChartEditorPropertyUI(XChartEditor xChartEditor) {
        this.xCreator = xChartEditor;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new ChartEditorDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
