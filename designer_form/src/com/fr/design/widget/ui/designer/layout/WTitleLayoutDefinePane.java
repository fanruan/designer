package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/3.
 */
public class WTitleLayoutDefinePane  extends AbstractDataModify<WTitleLayout> {
    private PaddingBoundPane paddingBoundPane;
    private UICheckBox displayECToolBar;
    public WTitleLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);
        paddingBoundPane = new PaddingBoundPane(creator);
        displayECToolBar = new UICheckBox(Inter.getLocText("FR-Designer_Widget_Display_Report_Tool"));
        advancePane.add(paddingBoundPane, BorderLayout.NORTH);
        advancePane.add(displayECToolBar, BorderLayout.CENTER);
        this.add(advanceExpandablePane);
    }

    @Override
    public String title4PopupWindow() {
        return "titleLayout";
    }

    @Override
    public void populateBean(WTitleLayout ob) {

    }


    @Override
    public WTitleLayout updateBean() {
        WTitleLayout layout = (WTitleLayout)creator.toData();
        return layout;
    }

}
