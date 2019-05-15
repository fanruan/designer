package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.RichStyleWidgetProvider;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;


/**
 * Created by ibm on 2017/8/3.
 */
public class PaddingBoundPane extends BasicPane {
    protected UISpinner top;
    protected UISpinner bottom;
    protected UISpinner left;
    protected UISpinner right;

    public PaddingBoundPane() {
        initBoundPane(0, 0, 0, 0);
    }

    public PaddingBoundPane(int top, int bottom, int left, int right) {
        initBoundPane(top, bottom, left, right);
    }

    public void initBoundPane(int t, int b, int l, int r) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        top = new UISpinner(0, Integer.MAX_VALUE, 1, t);
        bottom = new UISpinner(0, Integer.MAX_VALUE, 1, b);
        left = new UISpinner(0, Integer.MAX_VALUE, 1, l);
        right = new UISpinner(0, Integer.MAX_VALUE, 1, r);
        top.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"));
        bottom.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"));
        left.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"));
        right.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"));
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding_Duplicate"));
        label.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));
        label.setVerticalAlignment(SwingConstants.TOP);
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{label, createRightPane()}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1);
        this.add(panel);
    }


    public JPanel createRightPane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {f, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        Component[][] components1 = new Component[][]{
                new Component[]{top, bottom},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Top"), SwingConstants.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Bottom"), SwingConstants.CENTER)}
        };
        Component[][] components2 = new Component[][]{
                new Component[]{left, right},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Left"), SwingConstants.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Right"), SwingConstants.CENTER)}
        };
        JPanel northPanel = TableLayoutHelper.createGapTableLayoutPane(components1, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L6);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, IntervalConstants.INTERVAL_L1, 0));
        JPanel centerPanel = TableLayoutHelper.createGapTableLayoutPane(components2, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L6, IntervalConstants.INTERVAL_L6);
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, IntervalConstants.INTERVAL_L1, 0));
        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    public void update(RichStyleWidgetProvider marginWidget) {
        marginWidget.setMargin(updateBean());
    }

    public PaddingMargin updateBean() {
        return new PaddingMargin((int) top.getValue(), (int) left.getValue(), (int) bottom.getValue(), (int) right.getValue());
    }

    @Override
    protected String title4PopupWindow() {
        return "PaddingBoundPane";
    }

    public void populate(RichStyleWidgetProvider marginWidget) {
        populateBean(marginWidget.getMargin());
    }

    public void populateBean(PaddingMargin paddingMargin) {
        top.setValueWithoutEvent(paddingMargin.getTop());
        bottom.setValueWithoutEvent(paddingMargin.getBottom());
        left.setValueWithoutEvent(paddingMargin.getLeft());
        right.setValueWithoutEvent(paddingMargin.getRight());
    }
}
