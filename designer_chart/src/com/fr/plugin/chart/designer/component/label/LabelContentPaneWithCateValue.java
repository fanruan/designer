package com.fr.plugin.chart.designer.component.label;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * 分类和值（仪表盘的值标签）
 */
public class LabelContentPaneWithCateValue extends GaugeLabelContentPane {

    private static final long serialVersionUID = -8286902939543416431L;

    public LabelContentPaneWithCateValue(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, panel);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
                new Component[]{valueFormatPane,null},
        };
    }
}