package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.container.WCardLayout;


/**
 * Created by ibm on 2017/8/7.
 */
public class WCardLayoutDefinePane extends AbstractDataModify<WCardLayout> {

    public WCardLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WCardLayout ob) {

    }


    @Override
    public WCardLayout updateBean() {
        WCardLayout layout = (WCardLayout) creator.toData();
        return layout;
    }
}
