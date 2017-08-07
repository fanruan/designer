package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.*;
import com.fr.design.dialog.BasicPane;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/7/30.
 */

public class WidgetBoundPane extends BasicPane {
    protected XCreator creator;
    protected UISpinner width;
    protected UISpinner height;

    public WidgetBoundPane(XCreator source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creator = source;
        initBoundPane();
    }

    public XLayoutContainer getParent(XCreator source) {
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
        if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
            container = null;
        }
        return container;
    }


    public void initBoundPane() {

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        width = new UISpinner(0, 1200, 1);
        height = new UISpinner(0, 1200, 1);
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Widget_Size")), width, height},
                new Component[]{null, new UILabel(Inter.getLocText("FR-Designer-Tree_Width"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer-Tree_Height"), SwingConstants.CENTER)},
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p, f, f};
        int[][] rowCount = {{1, 1, 1}, {1, 1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 8, 5);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        UIExpandablePane uiExpandablePane = new UIExpandablePane("尺寸", 280, 20, panel);
        this.add(uiExpandablePane);
    }


    public void update() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        bounds.width = (int) width.getValue();
        bounds.height = (int) height.getValue();
        creator.setBounds(bounds);
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        width.setValue(bounds.width);
        height.setValue(bounds.height);
    }
}
