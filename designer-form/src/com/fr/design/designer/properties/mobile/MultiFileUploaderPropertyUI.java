package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XMultiFileUploader;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.impl.AbstractWidgetPropertyUIProvider;
import com.fr.design.gui.itable.AbstractPropertyTable;
import com.fr.design.widget.ui.designer.mobile.MultiFileUploaderDefinePane;
import com.fr.general.Inter;

/**
 * Created by plough on 2018/4/19.
 */
public class MultiFileUploaderPropertyUI extends AbstractWidgetPropertyUIProvider {

    private XCreator xCreator;

    public MultiFileUploaderPropertyUI(XMultiFileUploader xMultiFileUploader) {
        this.xCreator = xMultiFileUploader;
    }

    @Override
    public AbstractPropertyTable createWidgetAttrTable() {
        return null;
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new MultiFileUploaderDefinePane(xCreator);
    }

    @Override
    public String tableTitle() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
