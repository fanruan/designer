package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.design.widget.ui.designer.component.PaddingBoundPane;
import com.fr.design.widget.ui.designer.component.TabFitLayoutBackgroundPane;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/5.
 */
public class WTabFitLayoutDefinePane extends AbstractDataModify<WTabFitLayout> {
    private PaddingBoundPane paddingBoundPane;
    private TabFitLayoutBackgroundPane borderStyle;
    private UISpinner componentInterval;

    public WTabFitLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        paddingBoundPane = new PaddingBoundPane();
        borderStyle = new TabFitLayoutBackgroundPane();
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{new UILabel(Inter.getLocText("FR-Designer_Style")), borderStyle}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
        advancePane.add(jPanel, BorderLayout.NORTH);
        advancePane.add(paddingBoundPane, BorderLayout.CENTER);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.NORTH);
        initLayoutComponent();
    }

    public void initLayoutComponent(){
        componentInterval = new UISpinner(0, 100, 1, 0);
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{new UILabel(Inter.getLocText("FR-Designer_Component_Interval")), componentInterval}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
        this.add(jPanel, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WTabFitLayout ob) {
        borderStyle.populate(ob);
        paddingBoundPane.populate(ob);
        componentInterval.setValue(ob.getCompInterval());
    }


    @Override
    public WTabFitLayout updateBean() {
        WTabFitLayout layout = (WTabFitLayout) creator.toData();
        borderStyle.update(layout);
        paddingBoundPane.update(layout);
        layout.setCompInterval((int)componentInterval.getValue());
        return layout;
    }
}
