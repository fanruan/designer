package com.fr.design.widget;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by plough on 2017/8/7.
 */
public class WidgetBoundsPaneFactory {

    public static UIExpandablePane createBoundsPane(UISpinner width, UISpinner height) {
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Size")), createRightPane(width, height)},
                new Component[]{null, createRightPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tree_Width"), SwingConstants.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tree_Height"), SwingConstants.CENTER))},
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L6);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        boundsPane.add(panel);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Coords_And_Size"), 280, 24, boundsPane);
    }
    public static JPanel createRightPane(Component com1, Component com2){
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

    public static UIExpandablePane createAbsoluteBoundsPane(UISpinner x, UISpinner y, UISpinner width, UISpinner height) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;

        Component[][] northComponents = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Position")), createRightPane(x, y)},
                new Component[]{null, createRightPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_X_Coordinate"), SwingConstants.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Y_Coordinate"), SwingConstants.CENTER))},
        };
        Component[][] centerComponents = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Size")), createRightPane(width, height)},
                new Component[]{null, createRightPane(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tree_Width"), SwingConstants.CENTER), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tree_Height"), SwingConstants.CENTER))},
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}};
        final JPanel northPanel = TableLayoutHelper.createGapTableLayoutPane(northComponents, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L6);
        final JPanel centerPanel = TableLayoutHelper.createGapTableLayoutPane(centerComponents, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L6);
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        boundsPane.add(northPanel, BorderLayout.NORTH);
        boundsPane.add(centerPanel, BorderLayout.CENTER);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Coords_And_Size"), 230, 24, boundsPane);
    }


    public static UIExpandablePane createCardTagBoundPane(UISpinner width) {
        JPanel boundsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Size")), width},
        };
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L6);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        boundsPane.add(panel);
        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Coords_And_Size"), 280, 24, boundsPane);
    }
}
