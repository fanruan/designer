package com.fr.van.chart.designer;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.UIComponentUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.Component;
import java.util.Arrays;

/**
 * 布局 标题+组件
 */
public class TableLayout4VanChartHelper {

    private static final int SMALL_GAP = 20;
    public static final int EXPANDABLE_PANE_WIDTH =290;
    public static final int EXPANDABLE_PANE_HIGHT =24;
    public static final int DESCRIPTION_AREA_WIDTH = 60;
    public static final int EDIT_AREA_WIDTH =155;
    public static final int SECOND_EDIT_AREA_WIDTH = 143;
    public static final int COMPONENT_INTERVAL =12;
    public static final Border SECOND_EDIT_AREA_BORDER = BorderFactory.createEmptyBorder(0,12,0,0);

    public static JPanel createExpandablePaneWithTitleTopGap(String title, JPanel panel) {
        return new UIExpandablePane(title, EXPANDABLE_PANE_WIDTH, EXPANDABLE_PANE_HIGHT, panel) {
            protected void setcontentPanelontentPanelBorder() {
                getContentPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
            }
        };
    }

    public static JPanel createExpandablePaneWithTitle(String title, Component[][] components) {
        JPanel panel = createGapTableLayoutPane(components);
        return createExpandablePaneWithTitleTopGap(title, panel);
    }

    public static JPanel createExpandablePaneWithTitle(String title, JPanel panel) {
        return new UIExpandablePane(title, EXPANDABLE_PANE_WIDTH, EXPANDABLE_PANE_HIGHT, panel){
            protected void setcontentPanelontentPanelBorder (){
                getContentPanel().setBorder(BorderFactory.createEmptyBorder(0 ,5, 0, 0));
            }
        };
    }

    public static JPanel createGapTableLayoutPane(String title, Component component) {
        return createGapTableLayoutPane(title, component, EDIT_AREA_WIDTH);
    }

    public static JPanel createGapTableLayoutPane(String title, Component component, double componentWidth) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, componentWidth};
        double[] rowSize = {p, p};
        UILabel label = new UILabel(title);
        label.setVerticalAlignment(SwingConstants.TOP);
        UIComponentUtils.setLineWrap(label);
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{label, UIComponentUtils.wrapWithBorderLayoutPane(component)},
        };
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, COMPONENT_INTERVAL, LayoutConstants.VGAP_LARGE);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, EDIT_AREA_WIDTH};
        double[] rowSize = new double[components.length];
        Arrays.fill(rowSize, p);

        return createGapTableLayoutPane(components, rowSize, columnSize);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components,
                                                  double[] rowSize, double[] columnSize) {

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, COMPONENT_INTERVAL, LayoutConstants.VGAP_LARGE);
    }

    public static JPanel createGapTableLayoutPane(Component[][] components,
                                                  double[] rowSize, double[] columnSize, int[][] rowCount) {

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount,
                COMPONENT_INTERVAL, LayoutConstants.VGAP_LARGE);
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

    public static JPanel createTableLayoutPane(Component[][] components) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = new double[components.length];
        Arrays.fill(rowSize, p);

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

}