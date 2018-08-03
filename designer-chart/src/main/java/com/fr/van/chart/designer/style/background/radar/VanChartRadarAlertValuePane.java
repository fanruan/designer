package com.fr.van.chart.designer.style.background.radar;

import com.fr.design.gui.ilable.UILabel;

import com.fr.van.chart.designer.style.background.VanChartAlertValuePane;

import java.awt.Component;

/**
 * 雷达图的警戒线设置界面，没有坐标轴的选择。只给Y轴配置警戒线。
 */
public class VanChartRadarAlertValuePane extends VanChartAlertValuePane {
    private static final long serialVersionUID = -4732783185768672053L;

    protected Component[][] getTopPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-Use_Value")),alertValue},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")),alertLineStyle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Color_Color")),alertLineColor},
        };
    }

}