package com.fr.design.widget.ui.designer.layout;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
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
    private AccessibleWLayoutBorderStyleEditor borderStyleEditor;
    private PaddingBoundPane paddingBoundPane;
    private UICheckBox displayECToolBar;
    public WTitleLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        borderStyleEditor = new AccessibleWLayoutBorderStyleEditor();
        paddingBoundPane = new PaddingBoundPane();
        displayECToolBar = new UICheckBox(Inter.getLocText("FR-Designer_Widget_Display_Report_Tool"));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Style")), borderStyleEditor},
                new Component[]{paddingBoundPane, null},
                new Component[]{displayECToolBar, null}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, panel);

        this.add(advanceExpandablePane);

    }

    @Override
    public String title4PopupWindow() {
        return "titleLayout";
    }

    @Override
    public void populateBean(WTitleLayout ob) {
        paddingBoundPane.populate(ob);
//        displayECToolBar.setSelected(ob.);
    }


    @Override
    public WTitleLayout updateBean() {
        WTitleLayout layout = (WTitleLayout)creator.toData();
        return layout;
    }

}
