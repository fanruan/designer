package com.fr.plugin.chart.designer.component.label;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartLabelContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * 只有百分比（仪表盘的百分比标签）
 */
public class LabelContentPaneWithPercent extends VanChartLabelContentPane {
    private static final long serialVersionUID = -3739217668948747606L;

    public LabelContentPaneWithPercent(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, component);
    }

    protected double[] getRowSize(double p){
        return new double[]{p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{percentFormatPane, null},
        };
    }
}