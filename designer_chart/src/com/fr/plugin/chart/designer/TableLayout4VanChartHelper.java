package com.fr.plugin.chart.designer;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * 布局 标题+组件
 */
public class TableLayout4VanChartHelper {

    private static final int DEFAULT_GAP = 0;
    private static final int SMALL_GAP = 20;


    public static JPanel createExpandablePaneWithTitle(String title, JPanel panel) {
        return new UIExpandablePane(title, TableLayoutHelper.EXPANDABLE_PANE_WIDTH, TableLayoutHelper.EXPANDABLE_PANE_HIGHT, panel);
    }
    public static JPanel createExpandablePaneWithTitle(String title,  Component component) {
        JPanel panel = (JPanel) component;
        return new UIExpandablePane(title, TableLayoutHelper.EXPANDABLE_PANE_WIDTH, TableLayoutHelper.EXPANDABLE_PANE_HIGHT, panel);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, SMALL_GAP, LayoutConstants.VGAP_LARGE);
    }


    public static JPanel createGapTableLayoutPane(String title, Component component) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText(title)), component},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, SMALL_GAP, LayoutConstants.VGAP_LARGE);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components,
                                                  double[] rowSize, double[] columnSize) {

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, SMALL_GAP, LayoutConstants.VGAP_LARGE);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components,
                                                  double[] rowSize, double[] columnSize, int[][] rowCount) {

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount,
                SMALL_GAP, LayoutConstants.VGAP_LARGE);
    }

    /**
     * 标题布局(二级菜单距左边框46)
     * @param title  标题
     * @param component 组件
     * @return 布局好的组件
     */
    public static JPanel createTableLayoutPaneWithTitle(String title, Component component){
        return TableLayout4VanChartHelper.createTitlePane(title, component, LayoutConstants.CHART_ATTR_TOMARGIN);
    }

    /**
     * 标题布局(二级菜单距左边框46)
     * @param label  标题label
     * @param component 组件
     * @return 布局好的组件
     */
    public static JPanel createTableLayoutPaneWithUILabel(UILabel label, Component component){
        return TableLayout4VanChartHelper.createTitlePaneWithUILabel(label, component, LayoutConstants.CHART_ATTR_TOMARGIN);
    }

    /**
     * 标题布局(三级菜单距二级左侧20)
     * @param title  标题
     * @param component 组件
     * @return 布局好的组件
     */
    public static JPanel createTableLayoutPaneWithSmallTitle(String title, Component component){
        return TableLayout4VanChartHelper.createTitlePane(title, component, TableLayout4VanChartHelper.SMALL_GAP);
    }

    /**
     * 标题布局（指定gap）
     * @param title 标题
     * @param component 组件
     * @param gap 距左侧距离
     * @return 布局好的组件
     */
    public static JPanel createTitlePane(String title, Component component, int gap){
        return createTitlePaneWithUILabel(new UILabel(title), component, gap);
    }

    /**
     * 标题布局（指定gap）
     * @param label 标题label
     * @param component 组件
     * @param gap 距左侧距离
     * @return 布局好的组件
     */
    public static JPanel createTitlePaneWithUILabel(UILabel label, Component component, int gap){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {gap, f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{label,null},
                new Component[]{null,component},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

}