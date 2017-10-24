package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.AbstractMarginWidget;
import com.fr.form.ui.PaddingMargin;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;


/**
 * Created by ibm on 2017/8/3.
 */
public class PaddingBoundPane extends BasicPane{
    protected UISpinner top;
    protected UISpinner bottom;
    protected UISpinner left;
    protected UISpinner right;

    public PaddingBoundPane() {
        initBoundPane();
    }

    public void initBoundPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        top = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        bottom = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        left = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        right = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        top.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        bottom.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        left.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        right.setGlobalName(Inter.getLocText("FR-Designer_Layout-Padding"));
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Layout-Padding")), createRightPane(top, bottom)},
                new Component[]{null, createRightPane(new UILabel(Inter.getLocText("FR-Designer_Top"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Bottom"), SwingConstants.CENTER))},
                new Component[]{null, createRightPane(left, right)},
                new Component[]{null, createRightPane(new UILabel(Inter.getLocText("FR-Designer_Left"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Right"), SwingConstants.CENTER))},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L1, 0));
        this.add(panel);
    }

    public JPanel createRightPane(Component com1, Component com2){
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p};
        double[] columnSize = {f, f};
        int[][] rowCount = {{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{com1, com2}
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L1);
    }


    public void update(AbstractMarginWidget marginWidget) {
        marginWidget.setMargin(new PaddingMargin((int)top.getValue(), (int)left.getValue(), (int)bottom.getValue(), (int)right.getValue() ));
    }

    protected String title4PopupWindow() {
        return "PaddingBoundPane";
    }

    public void populate(AbstractMarginWidget marginWidget) {
        PaddingMargin paddingMargin = marginWidget.getMargin();
        top.setValue(paddingMargin.getTop());
        bottom.setValue(paddingMargin.getBottom());
        left.setValue(paddingMargin.getLeft());
        right.setValue(paddingMargin.getRight());
    }


}
