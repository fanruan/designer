package com.fr.plugin.chart.designer;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import javax.swing.*;
import java.awt.*;

/**
 * 布局 标题+组件
 */
public class TableLayout4VanChartHelper {

    private static final int SMALL_GAP = 20;
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