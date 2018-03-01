package com.fr.design.widget.ui.designer.component;

import com.fr.base.iofileattr.TemplateIdAttrMark;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.RichStyleWidgetProvider;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
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
        UILabel label = new UILabel(Inter.getLocText("FR-Designer_Layout-Padding"));
        label.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));
        label.setVerticalAlignment(SwingConstants.TOP);
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{label, createRightPane()}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1   );
        this.add(panel);
    }


    public JPanel createRightPane(){
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {f, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        Component[][] components1 = new Component[][]{
                new Component[]{top, bottom},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Top"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Bottom"), SwingConstants.CENTER)}
        };
        Component[][] components2 = new Component[][]{
                new Component[]{left, right},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Left"), SwingConstants.CENTER), new UILabel(Inter.getLocText("FR-Designer_Right"), SwingConstants.CENTER)}
        };
        JPanel northPanel = TableLayoutHelper.createGapTableLayoutPane(components1, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L6);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, IntervalConstants.INTERVAL_L1, 0));
        JPanel centerPanel = TableLayoutHelper.createGapTableLayoutPane(components2, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L6);
        JPanel panel =  FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L1, 0));
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }


    public void update(RichStyleWidgetProvider marginWidget) {
        marginWidget.setMargin(new PaddingMargin((int)top.getValue(), (int)left.getValue(), (int)bottom.getValue(), (int)right.getValue() ));
    }

    protected String title4PopupWindow() {
        return "PaddingBoundPane";
    }

    public void populate(RichStyleWidgetProvider marginWidget) {
        PaddingMargin paddingMargin = marginWidget.getMargin();
        top.setValue(paddingMargin.getTop());
        bottom.setValue(paddingMargin.getBottom());
        left.setValue(paddingMargin.getLeft());
        right.setValue(paddingMargin.getRight());
    }


}
