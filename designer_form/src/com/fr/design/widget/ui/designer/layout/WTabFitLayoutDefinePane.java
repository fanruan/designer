package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;
import java.awt.*;

/**
 * Created by ibm on 2017/8/5.
 */
public class WTabFitLayoutDefinePane extends AbstractDataModify<WTabFitLayout> {
    private PaddingBoundPane paddingBoundPane;

    public WTabFitLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        paddingBoundPane = new PaddingBoundPane();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, paddingBoundPane);
        this.add(advanceExpandablePane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WTabFitLayout ob) {

    }


    @Override
    public WTabFitLayout updateBean() {
        WTabFitLayout layout = (WTabFitLayout) creator.toData();
        return layout;
    }
}
