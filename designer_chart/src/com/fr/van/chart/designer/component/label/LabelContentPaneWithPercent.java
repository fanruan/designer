package com.fr.van.chart.designer.component.label;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 只有百分比（仪表盘的百分比标签）
 */
public class LabelContentPaneWithPercent extends GaugeLabelContentPane {
    private static final long serialVersionUID = -3739217668948747606L;

    public LabelContentPaneWithPercent(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, panel);
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