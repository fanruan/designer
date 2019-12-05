package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.container.WSortLayout;

/**
 * @author hades
 * @version 9.0
 * Created by hades on 2019/11/18
 */
public abstract class AbstractFRLayoutDefinePane<T extends WSortLayout> extends AbstractDataModify<T> {


    public AbstractFRLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
    }

    public AbstractFRLayoutDefinePane(XCreator xCreator, FormDesigner designer) {
        super(xCreator, designer);
    }

    protected void copyLayoutAttr(WSortLayout srcLayout, WSortLayout destLayout) {
        destLayout.clearListeners();
        destLayout.clearMobileWidgetList();
        for (int i = 0, len = srcLayout.getMobileWidgetListSize(); i < len; i++) {
            destLayout.addMobileWidget(srcLayout.getMobileWidget(i));
        }
        destLayout.setSorted(true);
        for (int i = 0, len = srcLayout.getListenerSize(); i < len; i++) {
            destLayout.addListener(srcLayout.getListener(i));
        }
    }

}
