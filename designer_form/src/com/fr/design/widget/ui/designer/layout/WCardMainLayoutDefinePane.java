package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import java.awt.*;

/**
 * Created by ibm on 2017/8/2.
 */
public class WCardMainLayoutDefinePane  extends AbstractDataModify<WCardMainBorderLayout> {

    public WCardMainLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        this.setPreferredSize(new Dimension(0,0));
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
    }

    @Override
    public String title4PopupWindow() {
        return "cardMainLayout";
    }

    @Override
    public void populateBean(WCardMainBorderLayout ob) {

    }


    @Override
    public WCardMainBorderLayout updateBean() {
        WCardMainBorderLayout layout = (WCardMainBorderLayout)creator.toData();
        return layout;
    }
}
