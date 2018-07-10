package com.fr.van.chart.designer.component.label;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 系列、值、值所占比例。（仪表盘的值标签）
 */
public class LabelContentPaneWithOutCate extends GaugeLabelContentPane {

    private static final long serialVersionUID = 1198062632979554467L;

    public LabelContentPaneWithOutCate(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, panel);
    }

    protected double[] getRowSize(double p){
        return new double[]{p,p,p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{seriesNameFormatPane, null},
                new Component[]{valueFormatPane, null},
                new Component[]{percentFormatPane, null},
        };
    }
}