package com.fr.van.chart.designer.style.background.radar;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.background.AlertLineListControlPane;
import com.fr.van.chart.designer.style.background.BackgroundListControlPane;
import com.fr.van.chart.designer.style.background.VanChartAxisAreaPane;

import javax.swing.JPanel;
import java.awt.Component;
import java.util.Arrays;

/**
 * 样式-背景-绘图区背景-雷达图只有Y轴的配置（间隔背景、网格线、警戒线）
 */
public class VanChartRadarAxisAreaPane extends VanChartAxisAreaPane {

    private static final long serialVersionUID = 2459614679918546393L;

    //雷达图只有横向的y轴的网格线配置
    @Override
    protected JPanel createGridLinePane() {

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")),horizontalGridLine},
        };
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] row = new double[components.length];
        Arrays.fill(row, p);
        double[] col = {f, e};
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Grid_Line"), panel);
    }

    protected Component[][] getIntervalPaneComponents() {
        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")),horizontalColorBackground},
        };
    }

    @Override
    protected AlertLineListControlPane getAlertLinePane() {
        return new AlertLineListControlPane(){
            protected Class<? extends BasicBeanPane> getAlertPaneClass() {
                return VanChartRadarAlertValuePane.class;
            }

            protected String[] getAlertAxisName(String[] axisNames) {
                return new String[]{axisNames[axisNames.length - 1]};//默认y轴，居左居右
            }
        };
    }

    @Override
    protected BackgroundListControlPane getBackgroundListControlPane() {
        return new BackgroundListControlPane(){
            protected Class<? extends BasicBeanPane> getIntervalPaneClass() {
                return VanChartRadarCustomIntervalBackgroundPane.class;
            }

            protected String[] getCustomIntervalBackgroundAxisName(String[] axisNames) {
                return new String[]{axisNames[axisNames.length - 1]};
            }
        };
    }
}