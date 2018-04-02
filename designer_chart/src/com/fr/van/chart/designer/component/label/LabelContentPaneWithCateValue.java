package com.fr.van.chart.designer.component.label;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

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